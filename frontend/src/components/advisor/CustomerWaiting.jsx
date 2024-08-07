import React from 'react';
import styled from 'styled-components';
import CustomerWaitingRooms from './CustomerWaitingRooms';

// 스타일 컴포넌트 정의
const CustomerWaitingSection = styled.div`
  border-radius: 1rem;
  background-color: #efefef;
  height: 100%;
  width: 100%;
`;
const ConnectionText = styled.div`
  font-family: 'CafeOhsquareAir';
`;

function CustomerWaiting({ connectedKiosks, setRooms, currentConnections, maxAvailable }) {
  // 대기 중인 인원 수 계산
  const people = connectedKiosks.filter(
    (connectedKiosk) => connectedKiosk.status === 'waiting'
  ).length;

  return (
    <div>
      <CustomerWaitingSection>
        <ConnectionText>
          현재 연결: ({currentConnections}/{maxAvailable})
        </ConnectionText>
        <CustomerWaitingRooms connectedKiosks={connectedKiosks} setRooms={setRooms} />
      </CustomerWaitingSection>
    </div>
  );
}

export default CustomerWaiting;
