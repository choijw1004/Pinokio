import React from 'react';
import styled from 'styled-components';

const CustomerScreen = styled.div`
  color: blue;
  font-size: 20px;
  width: 100%;
  height: 100%;
  border: solid black 1px;
  border-radius: 12px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
`;

function CustomerKiosk(props) {
  return (
    <div>
      <CustomerScreen>
        <p>유저가 보고있는 화면입니다.</p>
        <p>components/advisor/CustomerKiosk.jsx</p>
      </CustomerScreen>
    </div>
  );
}

export default CustomerKiosk;
