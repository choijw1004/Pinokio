import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  user: null,
  type: null,
  typeInfo: null,
  token: null,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUser: (state, action) => {
      console.log('action.payload:', action.payload); // 로그를 추가해 action.payload를 확인합니다.
      console.log('state before update:', state); // 로그를 추가해 상태를 확인합니다.
      state.user = action.payload.user; // state.user를 업데이트합니다.
      state.type = action.payload.type;
      state.typeInfo = action.payload.typeInfo;
      state.token = action.payload.token;
      console.log('state after update:', state); // 로그를 추가해 상태 업데이트 후 값을 확인합니다.
    },
    clearUser: (state) => {
      state.user = null;
      state.type = null;
      state.typeInfo = null;
      state.token = null;
    },
  },
});

export const { setUser, clearUser } = userSlice.actions;

export default userSlice.reducer;
