import React from 'react';
import styled from 'styled-components';
import CustomerWaitingRooms from './CustomerWaitingRooms';

// 스타일 컴포넌트 정의
const CustomerWaitingSection = styled.div`
  width: auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  flex: 1; /* 남은 공간을 차지하도록 설정 */
`;

function CustomerWaiting({ rooms, setRooms }) {
  // 대기 중인 인원 수 계산
  const people = rooms.filter((room) => room.status === 'waiting').length;

  return (
    <div>
      <CustomerWaitingSection>
        <p>대기 인원 ({people}/3)</p>
        <CustomerWaitingRooms rooms={rooms} setRooms={setRooms} />
      </CustomerWaitingSection>
    </div>
  );
}

export default CustomerWaiting;
