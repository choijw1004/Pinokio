import React from 'react';
import styled from 'styled-components';

const Room = styled.div`
  width: 200px;
  height: 230px;
  border: solid black 1px;
  border-radius: 20px;
  font-size: 12px;
  margin: 10px;
`;
function CustomerWaitingRooms({ rooms }) {
  return (
    <div style={{ display: 'flex' }}>
      {rooms.map((room, index) => (
        <Room key={index}>Room {room}</Room>
      ))}
    </div>
  );
}

export default CustomerWaitingRooms;
