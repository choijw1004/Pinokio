import React from 'react';
import styled from 'styled-components';

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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

function CustomerWaitingRooms({ rooms }) {
  return (
    <RoomsContainer>
      {rooms.map((room, index) => (
        <Room key={index} status={room.status}>
          Room {room.id} - {room.status}
        </Room>
      ))}
    </RoomsContainer>
  );
}

export default CustomerWaitingRooms;
