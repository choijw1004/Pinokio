import { createSlice } from '@reduxjs/toolkit';

// 최대 연결 가능한 고객 수
const MAX_CONNECTIONS = 3;

const advisorSlice = createSlice({
  name: 'advisor',
  initialState: {
    isAvailable: true, // 상담원이 새로운 연결을 받을 수 있는지 여부
    currentConnections: 0, // 현재 연결된 고객 수
  },
  reducers: {
    // 상담원의 가용성 상태를 설정하는 리듀서
    setAvailability: (state, action) => {
      state.isAvailable = action.payload;
    },
    // 연결된 고객 수를 증가시키는 리듀서
    incrementConnections: (state) => {
      if (state.currentConnections < MAX_CONNECTIONS) {
        state.currentConnections += 1;
        // 최대 연결 수에 도달하면 상담원을 unavailable 상태로 변경
        if (state.currentConnections === MAX_CONNECTIONS) {
          state.isAvailable = false;
        }
      }
    },
    // 연결된 고객 수를 감소시키는 리듀서
    decrementConnections: (state) => {
      if (state.currentConnections > 0) {
        state.currentConnections -= 1;
        // 연결 수가 최대 미만이 되면 상담원을 available 상태로 변경
        if (state.currentConnections < MAX_CONNECTIONS) {
          state.isAvailable = true;
        }
      }
    },
    // 모든 연결을 초기화하는 리듀서
    resetConnections: (state) => {
      state.currentConnections = 0;
      state.isAvailable = true;
    },
  },
});

// 액션 생성자들을 내보냄
export const { setAvailability, incrementConnections, decrementConnections, resetConnections } =
  advisorSlice.actions;

// 리듀서를 내보냄
export default advisorSlice.reducer;
