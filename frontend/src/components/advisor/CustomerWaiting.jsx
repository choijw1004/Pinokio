import React from 'react';
import styled from 'styled-components';
import CustomerWaitingRooms from './CustomerWaitingRooms';

// 스타일 컴포넌트 정의
const CustomerWaitingSection = styled.div`
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

function CustomerWaiting({ connectedKiosks, setRooms }) {
  // 대기 중인 인원 수 계산
  const people = connectedKiosks.filter(
    (connectedKiosks) => connectedKiosks.status === 'waiting'
  ).length;

  return (
    <div>
      <CustomerWaitingSection>
        <p>대기 인원 ({people}/3)</p>
        <CustomerWaitingRooms connectedKiosks={connectedKiosks} setRooms={setRooms} />
      </CustomerWaitingSection>
    </div>
  );
}

export default CustomerWaiting;
