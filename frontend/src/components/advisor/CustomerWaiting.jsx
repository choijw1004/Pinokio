import React from 'react';
import styled from 'styled-components';
import CustomerWaitingRooms from './CustomerWaitingRooms';

const CustomerWaitingSection = styled.div`
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

function CustomerWaiting({ connectedKiosks, subscribers, onDisconnect, onSetActiveKiosk }) {
  const waitingKiosks = connectedKiosks.filter((kiosk) => kiosk.status === 'connected');

  return (
    <CustomerWaitingSection>
      <p>
        대기 인원 ({waitingKiosks.length}/{connectedKiosks.length})
      </p>
      <CustomerWaitingRooms
        connectedKiosks={waitingKiosks}
        subscribers={subscribers}
        onDisconnect={onDisconnect}
        onSetActiveKiosk={onSetActiveKiosk}
      />
    </CustomerWaitingSection>
  );
}

export default CustomerWaiting;
