import React, { useState, useEffect, useRef } from 'react';
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
import Toast from '../../components/common/Toast';

// 스타일 컴포넌트 정의
const AdvMainPageWrapper = styled.div`
  display: flex;
  flex-direction: column;
  background-color: white;
  height: 1000px;
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
  height: 500px;
`;

const LeftSection = styled.div`
  display: flex;
  flex-direction: column;
  width: calc(40% - 0.5rem);
`;

const LeftTopSection = styled.div`
  width: 100%;
  height: calc(60% - 0.5rem);
`;

const LeftMiddleBarRef = styled.div`
  width: 100%;
  height: 1rem;
`;

const LeftBottomSection = styled.div`
  width: 100%;
  height: calc(40% - 0.5rem);
`;

const MiddleBar = styled.div`
  width: 1rem;
  background-color: blue;
  /* border: 1px solid black; */
  cursor: pointer;
  transform: ${(props) => `translateY(${props.deltaY}px)`};
`;

const RightSection = styled.div`
  display: flex;
  flex-direction: column;
  width: calc(60% - 0.5rem);
`;

const HeaderRight = styled.div`
  display: flex;
  align-items: center;
`;

const HeaderRightText = styled.div`
  font-family: 'CafeOhsquareAir';
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  margin: 0 1rem;
`;

const MaxButtonContainer = styled.div`
  display: flex;
`;

const ToggleContainer = styled.div`
  display: flex;
`;
const Box = styled.div`
  width: 100px;
  height: 100px;
  background-color: white;
`;

function AdvMainPage() {
  const advisorData = useSelector((state) => state.advisor);
  const userData = useSelector((state) => state.user);
  /*
    isAvailable : 거절 모드의 on / off 여부
    currentConnections : 현재 연결된 고객 수
  */
  const { isAvailable, currentConnections, maxConnections, roomToken, roomId, connectedKiosks } =
    advisorData;
  // maxAvailable : 최대 연결 가능 인원 수
  const [maxAvailable, setMaxAvailable] = useState(1);

  // deltaY : 가운데 Bar의 y축 이동 거리 (개발 예쩡)
  const [deltaY, setDeltaY] = useState(0);
  const handleScroll = (event) => {
    console.log('deedd');
    console.log(event.target.scrollTop);
    setDeltaY(event.target.scrollTop);
  };

  const middlebarRef = useRef();

  const leftmiddlebarRef = useRef();

  /*  
  isAccept : 연결 요청에 대한 답변
  요청이 오지 않아 toast가 뜨지 않은 상태는 "no request",
  요청이 들어왔지만 상담원이 아직 승낙/거절을 하지 않은 상태는 "waiting"
  요청에 대해 승낙을 하면 "accept"
  요청에 대해 거절을 하면 "reject"
  --> 일단 이렇게 써놨는데 만약 바꾸고 싶다면 바꾸되 이 주석도 그에 맞게 바꿔줘!!
  */
  const [isAccept, setIsAccept] = useState('waiting');

  const dispatch = useDispatch();

  useEffect(() => {
    console.log(userData);
    createRoom();
  }, []);

  useEffect(() => {
    middlebarRef.current.addEventListener('drag', (e) => {
      console.log('드래그하는 도중 발생하는 이벤트');
    });
    middlebarRef.current.addEventListener('dragend', (e) => {
      console.log('드래그가 끝나면 발생하는 이벤트');
    });
  }, []);

  useEffect(() => {
    console.log('Updated advisorData:', advisorData);
  }, [advisorData]);

  useEffect(() => {
    if (isAccept === 'accept') {
      // 승낙했으니 연결 과정으로 넘어간다.
      setIsAccept('no request');
    } else if (isAccept === 'reject') {
      // 거절에 따른 post를 보낸다.
      setIsAccept('no request');
    } else if (isAccept === 'no request') {
      // waiting request
    }
  }, [isAccept]);

  useEffect(() => {}, []);

  const createRoom = async () => {
    try {
      // const response = await makeMeetingRoom(userData.user.id);
      // console.log(`Room creation response:`, response);
      // dispatch(setRoomInfo(response));
      console.log(advisorData);
    } catch (error) {
      console.error(`Error creating room :`, error);
    }
  };

  const checkCanReceiveRequest = () => {
    // 현재 요청을 받을 수 있는 상태인지를 반환해준다.
    return isAccept === 'no request' && isAvailable && currentConnections < maxConnections;
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
            <HeaderRightText>최대 상담 인원 수</HeaderRightText>
            <UpDownButtons
              value={maxConnections}
              setValue={setMaxAvailable}
              color={'#7392ff'}
              size={'2rem'}
            />
          </MaxButtonContainer>
          <ToggleContainer>
            <HeaderRightText>거절 모드</HeaderRightText>
            <Toggle
              value={isAvailable}
              setValue={(value) => dispatch(setAvailability(value))}
              size={'3rem'}
            />
          </ToggleContainer>
        </HeaderRight>
      </AdvHeader>
      <AdvBody onScroll={handleScroll}>
        <LeftSection>
          <LeftTopSection>
            <CustomerVideo />
          </LeftTopSection>
          <LeftMiddleBarRef ref={leftmiddlebarRef} />
          <LeftBottomSection>
            <CustomerWaiting
              connectedKiosks={connectedKiosks}
              onConnect={handleCustomerConnect}
              onDisconnect={handleCustomerDisconnect}
              currentConnections={currentConnections}
              maxAvailable={maxConnections}
            />
          </LeftBottomSection>
        </LeftSection>
        <MiddleBar ref={middlebarRef} deltaY={deltaY} />
        <RightSection>
          <CustomerKiosk />
        </RightSection>
        {isAccept !== 'no request' && (
          <Toast
            message={'서비스 요청이 있습니다. 수락하시겠습니까?'}
            setAnswer={setIsAccept}
          ></Toast>
        )}
      </AdvBody>
    </AdvMainPageWrapper>
  );
}

export default AdvMainPage;
