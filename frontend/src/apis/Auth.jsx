import axios from './Axios';
/*
변수 네이밍 규칙
0. camelCase로 쓴다.
1. 행위를 맨 앞에 쓴다.
2. Auth의 API는 parameter가 없으므로 그냥 url을 이어서 쓴다.
*/

export const postRegisterAdvisor = async (code, username, password, confirmPassword) => {
  try {
    const response = await axios.post('/api/register/teller', {
      code: code,
      username: username,
      password: password,
      confirmPassword: confirmPassword,
    });
    return response.data;
  } catch (error) {
    console.error('register teller failed:', error);
    throw error;
  }
};

export const postRegisterPos = async (code, username, password, confirmPassword) => {
  try {
    const response = await axios.post('/api/register/pos', {
      code: code,
      username: username,
      password: password,
      confirmPassword: confirmPassword,
    });
    return response.data;
  } catch (error) {
    console.error('register pos failed:', error);
    throw error;
  }
};

export const postRegisterKiosk = async (posId) => {
  try {
    const response = await axios.post('/api/register/kiosk', {
      posId: posId,
    });
    return response.data;
  } catch (error) {
    console.error('register kiosk failed:', error);
    throw error;
  }
};

export const postLoginAdvisor = async (username, password) => {
  try {
    const response = await axios.post('/api/login/teller', {
      username: username,
      password: password,
    });
    return response.data;
  } catch (error) {
    console.error('Teller login failed:', error);
    throw error;
  }
};

export const postLoginPos = async (username, password) => {
  try {
    console.log('Request payload:', { username, password });
    const response = await axios.post('/api/login/pos', {
      username: username,
      password: password,
    });
    // 응답 데이터 로그로 확인
    console.log('PostLoginPos response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Pos login failed:', error);
    throw error;
  }
};

export const postLoginKiosk = async (username, password) => {
  try {
    const response = await axios.post('/api/login/kiosk', {
      username: username,
      password: password,
    });
    return response.data;
  } catch (error) {
    console.error('Kiosk login failed:', error);
    throw error;
  }
};

export const getKioskInfo = async () => {
  try {
    const response = await axios.get('/api/kiosk/my-info');
    return response.data;
  } catch (error) {
    console.error('get Kiosk info failed:', error);
    throw error;
  }
};

export const getPosInfo = async () => {
  try {
    const response = await axios.get('/api/pos/my-info');
    return response.data;
  } catch (error) {
    console.error('get pos info failed:', error);
    throw error;
  }
};
