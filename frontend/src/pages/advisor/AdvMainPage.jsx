import React, { useState, useEffect } from 'react';
import Logo from '../../components/common/Logo';
import CustomerVideo from '../../components/advisor/CustomerVideo';
import CustomerKiosk from '../../components/advisor/CustomerKiosk';
import CustomerWaiting from '../../components/advisor/CustomerWaiting';
import Toggle from '../../components/common/Toggle';
import styled from 'styled-components';
import { makeMeetingRoom } from '../../apis/Room';
import { useDispatch, useSelector } from 'react-redux';
import {
  setAvailability,
  setRoomInfo,
  connectKiosk,
  disconnectKiosk,
} from '../../features/advisor/AdvisorSlice';
import UpDownButtons from '../../components/common/UpDownButtons';

// 스타일 컴포넌트 정의
const AdvMainPageWrapper = styled.div`
  display: flex;
  flex-direction: column;
  background-color: white;
  overflow: hidden; /* 스크롤 제거 */
`;

const AdvHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const AdvBody = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
`;

const LeftSection = styled.div`
  display: flex;
  flex-direction: column;
  width: 40%;
`;

const LeftTopSection = styled.div`
  width: 100%;
  height: 60%;
`;

const LeftBottomSection = styled.div`
  width: 100%;
  height: 40%;
`;

const RightSection = styled.div`
  display: flex;
  flex-direction: column;
  width: 60%;
`;

const HeaderRight = styled.div`
  display: flex;
  align-items: center;
`;

const MaxButtonContainer = styled.div`
  display: flex;
`;

const ToggleContainer = styled.div`
  display: flex;
`;

function AdvMainPage() {
  const advisorData = useSelector((state) => state.advisor);
  const userData = useSelector((state) => state.user);
  const { isAvailable, currentConnections, roomToken, roomId, connectedKiosks } = advisorData;
  const [maxAvailable, setMaxAvailable] = useState(1);
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
    <AdvMainPageWrapper>
      <AdvHeader>
        <Logo size={'2.5rem'} />
        <HeaderRight>
          <MaxButtonContainer>
            <UpDownButtons
              value={maxAvailable}
              setValue={setMaxAvailable}
              color={'#7392ff'}
              size={'2rem'}
            />
          </MaxButtonContainer>
          <ToggleContainer>
            <Toggle
              value={isAvailable}
              setValue={(value) => dispatch(setAvailability(value))}
              size={'5rem'}
            />
            <p>연결 거절모드 토글 (현재 연결: {currentConnections}/3)</p>
          </ToggleContainer>
        </HeaderRight>
      </AdvHeader>
      <AdvBody>
        <LeftSection>
          <LeftTopSection>
            <CustomerVideo>
              <p>연결 대기중</p>
              <p>components/advisor/CustomerVideo.jsx</p>
            </CustomerVideo>
          </LeftTopSection>
          <LeftBottomSection>
            <CustomerWaiting
              connectedKiosks={connectedKiosks}
              onConnect={handleCustomerConnect}
              onDisconnect={handleCustomerDisconnect}
            />
          </LeftBottomSection>
        </LeftSection>
        <RightSection>
          <CustomerKiosk />
        </RightSection>
      </AdvBody>
    </AdvMainPageWrapper>
  );
}

export default AdvMainPage;
