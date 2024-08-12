import React from 'react';
import styled from 'styled-components';
import CustomerWaitingRooms from './CustomerWaitingRooms';

// 스타일 컴포넌트 정의
const CustomerWaitingSection = styled.div`
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

function CustomerWaiting({ connectedKiosks, subscribers, onDisconnect, activeKiosk }) {
  const waitingKiosks = connectedKiosks.filter(
    (kiosk) => kiosk.status === 'connected' && kiosk.connectionId !== activeKiosk
  );

  return (
    <CustomerWaitingSection>
      <p>대기 인원 ({waitingKiosks.length}/2)</p>
      <CustomerWaitingRooms
        connectedKiosks={waitingKiosks}
        subscribers={subscribers}
        onDisconnect={onDisconnect}
      />
    </CustomerWaitingSection>
  );
}

export default CustomerWaiting;
