import React from 'react';
import styled from 'styled-components';
import { useState } from 'react';

import CustomerWaitingRooms from './CustomerWaitingRooms';

const CustomerWaitingSection = styled.div`
  width: 695px;
  height: 300px;
  border: solid black 1px;
  border-radius: 12px;
  text-align: center;
`;

function CustomerWaiting(props) {
  const [rooms, useRooms] = useState([1, 2, 3]);

  return (
    <div>
      <CustomerWaitingSection>
        <p>고객들 대기화면입니다.</p>
        <CustomerWaitingRooms rooms={rooms} />
      </CustomerWaitingSection>
    </div>
  );
}

export default CustomerWaiting;
