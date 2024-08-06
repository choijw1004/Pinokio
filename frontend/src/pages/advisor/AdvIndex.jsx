import React, { useState, useEffect } from 'react';
import Navbar from '../../components/advisor/Navbar';
import CustomerKiosk from '../../components/advisor/CustomerKiosk';
import CustomerVideo from '../../components/advisor/CustomerVideo';
import CustomerWaiting from '../../components/advisor/CustomerWaiting';
import ToggleSwitch from '../../components/advisor/ToggleSwitch';
import styled from 'styled-components';
import { makeMeetingRoom } from '../../apis/Room';
import { useDispatch, useSelector } from 'react-redux';
import {
  setAvailability,
  setRoomInfo,
  connectKiosk,
  disconnectKiosk,
} from '../../features/advisor/AdvisorSlice';

// 스타일 컴포넌트 정의
const AdvIndexWrapper = styled.div`
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 20px;
  background-color: #f4f4f9;
  overflow: hidden; /* 스크롤 제거 */
`;

const MainContent = styled.div`
  display: flex;
  flex: 1;
  margin-top: 20px;
  overflow: hidden; /* 스크롤 제거 */
`;

const LeftSection = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-right: 10px; /* 오른쪽 여백 추가 */
`;

const RightSection = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-left: 10px; /* 왼쪽 여백 추가 */
  overflow: auto; /* 내용이 짤리지 않도록 스크롤 추가 */
`;

const NavComponents = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const ToggleContainer = styled.div`
  display: flex;
  align-items: center;
`;

function AdvIndex() {
  const advisorData = useSelector((state) => state.advisor);
  const userData = useSelector((state) => state.user);
  const { isAvailable, currentConnections, roomToken, roomId, connectedKiosks } = advisorData;
  const dispatch = useDispatch();

  useEffect(() => {
    console.log(userData);
    createRoom();
  }, []);

  useEffect(() => {
    console.log('Updated advisorData:', advisorData);
  }, [advisorData]);

  const createRoom = async () => {
    try {
      const response = await makeMeetingRoom(userData.user.id);
      console.log(`Room creation response:`, response);
      dispatch(setRoomInfo(response));
      console.log(advisorData);
    } catch (error) {
      console.error(`Error creating room :`, error);
    }
  };
  const handleCustomerConnect = (customerId, roomId) => {
    console.log(customerId, roomId);
  };

  const handleCustomerDisconnect = (customerId, roomId) => {
    console.log(customerId, roomId);
  };

  return (
    <AdvIndexWrapper>
      <NavComponents>
        <Navbar />
        <ToggleContainer>
          <ToggleSwitch
            isAvailable={isAvailable}
            setIsAvailable={(value) => dispatch(setAvailability(value))}
          />
          <p>연결 거절모드 토글 (현재 연결: {currentConnections}/3)</p>
        </ToggleContainer>
      </NavComponents>
      <MainContent>
        <LeftSection>
          <CustomerVideo />
          <CustomerWaiting
            connectedKiosks={connectedKiosks}
            onConnect={handleCustomerConnect}
            onDisconnect={handleCustomerDisconnect}
          />
        </LeftSection>
        <RightSection>
          <CustomerKiosk />
        </RightSection>
      </MainContent>
    </AdvIndexWrapper>
  );
}

export default AdvIndex;
