import { createSlice } from '@reduxjs/toolkit';

// 최대 연결 가능한 고객 수
const MAX_CONNECTIONS = 3;

const createInitialKioskState = (id) => ({ id, status: 'waiting', kioskId: null });

const advisorSlice = createSlice({
  name: 'advisor',
  initialState: {
    isAvailable: true, // 상담원이 새로운 연결을 받을 수 있는지 여부
    currentConnections: 0, // 현재 연결된 고객 수
    roomToken: null, // 방 토큰
    roomId: null, // 방 ID
    connectedKiosks: [
      createInitialKioskState(1),
      createInitialKioskState(2),
      createInitialKioskState(3),
    ], // 연결된 키오스크 정보 배열
  },
  reducers: {
    setAvailability: (state, action) => {
      state.isAvailable = action.payload;
    },
    setRoomInfo: (state, action) => {
      const { roomId, token } = action.payload;
      state.roomToken = token;
      state.roomId = roomId;
    },
    connectKiosk: (state, action) => {
      const { id, kioskId } = action.payload;
      const availableSlot = state.connectedKiosks.find((kiosk) => kiosk.status === 'waiting');
      if (availableSlot && state.currentConnections < MAX_CONNECTIONS) {
        availableSlot.kioskId = kioskId;
        availableSlot.status = 'connected';
        state.currentConnections += 1;
        if (state.currentConnections === MAX_CONNECTIONS) {
          state.isAvailable = false;
        }
      }
    },
    disconnectKiosk: (state, action) => {
      const kioskId = action.payload;
      const connectedKiosk = state.connectedKiosks.find((kiosk) => kiosk.kioskId === kioskId);
      if (connectedKiosk) {
        connectedKiosk.status = 'waiting';
        connectedKiosk.kioskId = null;
        state.currentConnections -= 1;
        if (state.currentConnections < MAX_CONNECTIONS) {
          state.isAvailable = true;
        }
      }
    },
    resetAdvisor: (state) => {
      state.isAvailable = true;
      state.currentConnections = 0;
      state.roomToken = null;
      state.roomId = null;
      state.connectedKiosks = state.connectedKiosks.map((kiosk, index) =>
        createInitialKioskState(index + 1)
      );
    },
  },
});

export const { setAvailability, setRoomInfo, connectKiosk, disconnectKiosk, resetAdvisor } =
  advisorSlice.actions;

export default advisorSlice.reducer;
