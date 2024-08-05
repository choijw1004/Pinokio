import { createSlice } from '@reduxjs/toolkit';

const roomSlice = createSlice({
  name: 'room',
  initialState: {
    roomData: [
      // 각 방의 초기 상태를 정의합니다.
      {
        id: 1,
        isActive: false, // 방이 활성화되었는지 여부
        isAdvising: false, // 현재 상담 중인지 여부
        roomToken: null, // 방 토큰
        roomId: null, // 방 ID
        connectedKiosk: null, // 연결된 키오스크 정보
      },
      // 방 2와 3도 동일한 구조로 초기화
      {
        id: 2,
        isActive: false,
        isAdvising: false,
        roomToken: null,
        roomId: null,
        connectedKiosk: null,
      },
      {
        id: 3,
        isActive: false,
        isAdvising: false,
        roomToken: null,
        roomId: null,
        connectedKiosk: null,
      },
    ],
  },
  reducers: {
    // 방을 활성화하고 토큰과 ID를 설정하는 리듀서
    setRoomActive: (state, action) => {
      const { id, roomToken, roomId } = action.payload;
      const room = state.roomData.find((room) => room.id === id);
      if (room) {
        room.isActive = true;
        room.roomToken = roomToken;
        room.roomId = roomId;
      }
    },
    // 방을 상담 중 상태로 설정하고 연결된 키오스크 정보를 저장하는 리듀서
    setRoomAdvising: (state, action) => {
      const { id, kioskInfo } = action.payload;
      const room = state.roomData.find((room) => room.id === id);
      if (room) {
        room.isAdvising = true;
        room.connectedKiosk = kioskInfo;
      }
    },
    // 방의 상태를 초기화하는 리듀서
    resetRoom: (state, action) => {
      const { id } = action.payload;
      const room = state.roomData.find((room) => room.id === id);
      if (room) {
        room.isAdvising = false;
        room.connectedKiosk = null;
        room.roomId = null;
        room.roomToken = null;
      }
    },
  },
});

// 액션 생성자들을 내보냅니다.
export const { setRoomActive, setRoomAdvising, resetRoom } = roomSlice.actions;

// 리듀서를 내보냅니다.
export default roomSlice.reducer;
