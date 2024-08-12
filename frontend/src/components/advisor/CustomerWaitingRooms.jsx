import React from 'react';
import styled from 'styled-components';
import OpenViduVideoComponent from '../advisor/OpenViduComponent';

const RoomsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
`;

const Room = styled.div`
  width: 30%;
  height: 150px;
  border: 1px solid #ccc;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: ${(props) =>
    props.$status === 'waiting'
      ? '#f0f0f0'
      : props.$status === 'requested'
      ? '#ffe0b2'
      : props.$status === 'connected'
      ? '#c8e6c9'
      : 'white'};
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

const VideoContainer = styled.div`
  width: 100%;
  height: 100px;
  overflow: hidden;
`;

const DisconnectButton = styled.button`
  margin-top: 5px;
  padding: 2px 5px;
  background-color: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #ff3333;
  }
`;

function CustomerWaitingRooms({ connectedKiosks, subscribers, onDisconnect }) {
  return (
    <RoomsContainer>
      {connectedKiosks.map((kiosk) => {
        const subscriber = subscribers.find(
          (sub) => sub.stream.connection.connectionId === kiosk.connectionId
        );
        return (
          <Room key={kiosk.id} $status={kiosk.status}>
            <p>Room {kiosk.id}</p>
            <VideoContainer>
              {subscriber && <OpenViduVideoComponent streamManager={subscriber} />}
            </VideoContainer>
            {kiosk.status === 'connected' && (
              <DisconnectButton onClick={() => onDisconnect(kiosk.connectionId)}>
                Disconnect
              </DisconnectButton>
            )}
          </Room>
        );
      })}
    </RoomsContainer>
  );
}

export default CustomerWaitingRooms;
