import { createSlice } from '@reduxjs/toolkit';

const createInitialKioskState = (id) => ({
  id,
  status: 'waiting',
  kioskId: null,
  connectionId: null,
  isActive: false,
});

const advisorSlice = createSlice({
  name: 'advisor',
  initialState: {
    isAvailable: true,
    currentConnections: 0,
    roomToken: null,
    roomId: null,
    connectedKiosks: [
      createInitialKioskState(1),
      createInitialKioskState(2),
      createInitialKioskState(3),
    ],
    maxConnections: 3,
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
      const { kioskId } = action.payload;
      const targetKiosk = state.connectedKiosks.find((kiosk) => kiosk.status === 'waiting');
      if (targetKiosk) {
        targetKiosk.kioskId = kioskId;
        targetKiosk.status = 'connected';
        if (state.currentConnections === 0) {
          targetKiosk.isActive = true;
        }
        if (state.currentConnections < state.maxConnections) {
          state.currentConnections += 1;
          if (state.currentConnections === state.maxConnections) {
            state.isAvailable = false;
          }
        }
      }
    },
    updateKiosk: (state, action) => {
      const { connectionId } = action.payload;
      const targetKiosk = state.connectedKiosks.find(
        (kiosk) => kiosk.status === 'connected' && !kiosk.connectionId
      );
      if (targetKiosk) {
        targetKiosk.connectionId = connectionId;
      }
    },
    disconnectKiosk: (state, action) => {
      const connectionId = action.payload;
      const connectedKiosk = state.connectedKiosks.find(
        (kiosk) => kiosk.connectionId === connectionId
      );
      if (connectedKiosk) {
        const wasActive = connectedKiosk.isActive;
        connectedKiosk.status = 'waiting';
        connectedKiosk.kioskId = null;
        connectedKiosk.connectionId = null;
        connectedKiosk.isActive = false;
        state.currentConnections -= 1;
        if (state.currentConnections < state.maxConnections) {
          state.isAvailable = true;
        }
        if (wasActive && state.currentConnections > 0) {
          const newActiveKiosk = state.connectedKiosks.find(
            (kiosk) => kiosk.status === 'connected'
          );
          if (newActiveKiosk) {
            newActiveKiosk.isActive = true;
          }
        }
      }
    },
    setActiveKiosk: (state, action) => {
      const connectionId = action.payload;
      state.connectedKiosks.forEach((kiosk) => {
        kiosk.isActive = kiosk.connectionId === connectionId;
      });
    },
    resetAdvisor: (state) => {
      state.isAvailable = true;
      state.currentConnections = 0;
      state.roomToken = null;
      state.roomId = null;
      state.connectedKiosks = state.connectedKiosks.map((_, index) =>
        createInitialKioskState(index + 1)
      );
    },
  },
});

export const {
  setAvailability,
  setRoomInfo,
  connectKiosk,
  updateKiosk,
  disconnectKiosk,
  setActiveKiosk,
  resetAdvisor,
} = advisorSlice.actions;

export default advisorSlice.reducer;
