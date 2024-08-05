import React from 'react';
import Navbar from '../../components/advisor/Navbar';
import CustomerKiosk from '../../components/advisor/CustomerKiosk';
import CustomerVideo from '../../components/advisor/CustomerVideo';
import CustomerWaiting from '../../components/advisor/CustomerWaiting';
import ToggleSwitch from '../../components/advisor/ToggleSwitch';
import styled from 'styled-components';

const Components = styled.div`
  display: flex;
`;

const LeftComponents = styled.div`
  margin-right: 10px;
`;

const NavComponents = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
`;

function AdvLoadingPage() {
  return (
    <div>
      <NavComponents>
        <Navbar />
        <div className="toggle" style={{ display: 'flex' }}>
          <ToggleSwitch />
          <p>연결 거절모드 토글</p>
        </div>
      </NavComponents>
      <Components>
        <LeftComponents>
          <CustomerVideo />
          <CustomerWaiting />
        </LeftComponents>
        <CustomerKiosk />
      </Components>
    </div>
  );
}

export default AdvLoadingPage;
