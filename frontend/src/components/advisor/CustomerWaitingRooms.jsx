import React from 'react';
import styled from 'styled-components';

const RoomsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  padding: 0 15px;
`;

const Room = styled.div`
  width: 32%;
  height: 150px;

  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${(props) =>
    props.status === 'waiting'
      ? '#f0f0f0'
      : props.status === 'requested'
      ? '#ffe0b2'
      : props.status === 'connected'
      ? '#c8e6c9'
      : 'white'};
  background-color: #d9d9d9;
  font-family: 'CafeOhsquareAir';
`;

function CustomerWaitingRooms({ connectedKiosks }) {
  return (
    <RoomsContainer>
      {connectedKiosks.map((connectedKiosks, index) => (
        <Room key={index} status={connectedKiosks.status}>
          Room {connectedKiosks.id} - {connectedKiosks.status}
        </Room>
      ))}
    </RoomsContainer>
  );
}

export default CustomerWaitingRooms;
