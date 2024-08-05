import axios from './Axios'; // 인스턴스와 구분하기 위해 대문자 사용

/*
변수 네이밍 규칙
0. 카멜 케이스로 쓴다.
1. 행위를 맨 앞에 쓴다.
2. 가져오는 대상을 다음에 쓴다.
3. 가져오는 대상이 리스트라면 복수형으로 쓴다.
*/

export const makeMeetingRoom = async (tellerId) => {
  try {
    const response = await axios.post(`/api/meeting/teller/${tellerId}`);
    return response.data;
  } catch (error) {
    console.error('makeMeetingRoom error', error);
    throw error;
  }
};

// export const getMeetingRequest = async (tellerId) => {
//   try {
//     const response = await axios.get(`/api/meeting/teller/${tellerId}`);
//     return response.data;
//   } catch (error) {
//     console.error('getMeetingRequest error', error);
//     throw error;
//   }
// };

export const requestMeeting = async (kioskId) => {
  try {
    const response = await axios.post(`/api/meeting/kiosk/${kioskId}/request-enter`, {
      params: { kioskId: kioskId },
    });
    return response.data;
  } catch (error) {
    console.error('requestMeeting error', error);
    throw error;
  }
};
