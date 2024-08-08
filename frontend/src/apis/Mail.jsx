import axios from './Axios';

// 인증 메일 전송
export const sendEmail = async () => {
  try {
    const response = await axios.post(`/api/mail/send`);
    return response.data;
  } catch (error) {
    console.error('Failed to send email:', error);
    throw error;
  }
};

// 인증 번호 확인
export const checkAuth = async () => {
  try {
    const response = await axios.post('/api/mail/check-auth');
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 상담원 새로운 비밀번호 전송
export const sendTellerNewPassword = async () => {
  try {
    const response = await axios.post(`/api/mail/send/teller/new-password`);
    return response.data;
  } catch (error) {
    console.error('Failed to send teller new password:', error);
    throw error;
  }
};

// 포스 새로운 비밀번호 전송
export const sendPosNewPassword = async () => {
  try {
    const response = await axios.post(`/api/mail/send/pos/new-password`);
    return response.data;
  } catch (error) {
    console.error('Failed to send pos new password:', error);
    throw error;
  }
};
