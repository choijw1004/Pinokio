import React, { useState, useEffect, useCallback, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import styled from 'styled-components';
import Modal from 'react-modal';
import Logo from '../../components/common/Logo';
import CustomerVideo from '../../components/advisor/CustomerVideo';
import CustomerKiosk from '../../components/advisor/CustomerKiosk';
import CustomerWaiting from '../../components/advisor/CustomerWaiting';
import Toggle from '../../components/common/Toggle';
import useWebSocket from '../../hooks/useWebSocket';
import { makeMeetingRoom, acceptMeeting, deleteMeetingRoom, rejectMeeting } from '../../apis/Room';
import {
  setAvailability,
  setRoomInfo,
  connectKiosk,
  updateKiosk,
  disconnectKiosk,
  setActiveKiosk,
  resetAdvisor,
} from '../../features/advisor/AdvisorSlice';
import { OpenVidu } from 'openvidu-browser';

Modal.setAppElement('#root');
import Toast from '../../components/common/Toast';
import { clearUser } from '../../features/user/userSlice';
import Cookies from 'js-cookie';

import { Resizable } from 'react-resizable';

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

const AdvMainPage = () => {
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
  const [showModal, setShowModal] = useState(false);
  const [consultationRequest, setConsultationRequest] = useState(null);
  const { sendMessage, lastMessage, isConnected, connect } = useWebSocket(userData.token);

  const [OV, setOV] = useState(null);
  const [session, setSession] = useState(null);
  const [publisher, setPublisher] = useState(null);
  const [subscribers, setSubscribers] = useState([]);
  const [isInitialized, setIsInitialized] = useState(false);

  const initializeAdvisor = useCallback(async () => {
    if (!isConnected) {
      try {
        const response = await makeMeetingRoom();
        console.log(`방 생성 응답:`, response);
        dispatch(setRoomInfo(response));
        connect();
      } catch (error) {
        console.error(`방 생성 오류:`, error);
      }
    }
  }, [dispatch, connect, isConnected]);

  const handleCustomerDisconnect = useCallback(
    (connectionId) => {
      dispatch(disconnectKiosk(connectionId));
      setSubscribers((prevSubscribers) =>
        prevSubscribers.filter((sub) => sub.stream.connection.connectionId !== connectionId)
      );
    },
    [dispatch]
  );

  const handleCustomerConnect = useCallback(
    (connectionId) => {
      dispatch(
        updateKiosk({
          connectionId,
        })
      );
      console.log(`Attempting to update kiosk with connectionId: ${connectionId}`);
    },
    [dispatch]
  );

  const initializeSession = useCallback(
    async (roomId, token) => {
      const ov = new OpenVidu();
      setOV(ov);

      const session = ov.initSession();
      setSession(session);

      session.on('streamCreated', (event) => {
        const subscriber = session.subscribe(event.stream, undefined);
        setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
        console.log(subscriber);
        const connectionId = event.stream.connection.connectionId;
        handleCustomerConnect(connectionId);
        console.log(`New subscriber added: ${connectionId}`);
      });

      session.on('streamDestroyed', (event) => {
        const connectionId = event.stream.connection.connectionId;
        handleCustomerDisconnect(connectionId);
      });

      try {
        await session.connect(token, { clientData: userData.email });
        console.log('OpenVidu 세션 연결 성공');

        const publisher = await ov.initPublisherAsync(undefined, {
          audioSource: undefined,
          videoSource: undefined,
          publishAudio: true,
          publishVideo: true,
          resolution: '640x480',
          frameRate: 30,
          insertMode: 'APPEND',
          mirror: false,
        });

        await session.publish(publisher);
        setPublisher(publisher);
        console.log('상담원 스트림 발행 성공');
      } catch (error) {
        console.error('세션 연결 또는 스트림 발행 오류:', error);
      }
    },
    [userData.email, handleCustomerConnect, handleCustomerDisconnect]
  );

  useEffect(() => {
    if (userData.token && !isConnected) {
      initializeAdvisor();
    }
    return () => {
      dispatch(resetAdvisor());
    };
  }, [initializeAdvisor, userData.token, dispatch, isConnected]);

  useEffect(() => {
    if (roomId && roomToken && !isInitialized) {
      initializeSession(roomId, roomToken);
      setIsInitialized(true);
    }
  }, [roomId, roomToken, initializeSession, isInitialized]);

  useEffect(() => {
    middlebarRef.current.addEventListener('drag', (e) => {
      console.log('드래그하는 도중 발생하는 이벤트');
    });
    middlebarRef.current.addEventListener('dragend', (e) => {
      console.log('드래그가 끝나면 발생하는 이벤트');
    });
  }, []);

  useEffect(() => {
    if (lastMessage) {
      const data = JSON.parse(lastMessage.data);
      console.log('WebSocket 메시지 수신:', data);
      if (data.type === 'consultationRequest') {
        handleConsultationRequest(data);
      }
    }
  }, [lastMessage]);

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

  const handleConsultationRequest = (data) => {
    console.log('상담 요청 받음:', data);
    console.log(isAvailable, currentConnections, maxConnections);
    if (isAvailable && currentConnections < maxConnections) {
      setConsultationRequest(data);
      setShowModal(true);
    }
  };

  const handleAcceptMeeting = async () => {
    try {
      await acceptMeeting(roomId, consultationRequest.kioskId);
      const availableRoom = connectedKiosks.find((kiosk) => kiosk.status === 'waiting');
      if (availableRoom) {
        dispatch(
          connectKiosk({
            id: availableRoom.id,
            kioskId: consultationRequest.kioskId,
            status: 'connected',
          })
        );
        console.log('Updated connectedKiosks:', connectedKiosks);
      } else {
        console.log('No available room for new kiosk');
      }
      setShowModal(false);
      setConsultationRequest(null);
    } catch (error) {
      console.error('상담 수락 오류:', error);
    }
  };

  const handleRejectMeeting = async () => {
    try {
      await rejectMeeting();
    } catch (error) {
      console.error('상담 거절 오류:', error);
    }
    setShowModal(false);
    setConsultationRequest(null);
  };

  const handleSetActiveKiosk = useCallback(
    (connectionId) => {
      dispatch(setActiveKiosk(connectionId));
      subscribers.forEach((subscriber) => {
        if (subscriber.stream.connection.connectionId === connectionId) {
          subscriber.subscribeToAudio(true);
        } else {
          subscriber.subscribeToAudio(false);
        }
      });
    },
    [dispatch, subscribers]
  );

  const activeKiosk = connectedKiosks.find((kiosk) => kiosk.isActive);
  const activeSubscriber = subscribers.find(
    (sub) => activeKiosk && sub.stream.connection.connectionId === activeKiosk.connectionId
  );

  return (
    <AdvMainPageWrapper>
      <AdvHeader>
        <HeaderLeft>
          <Logo size={'2.5rem'} />
          <LogOut
            onClick={() => {
              handleOnclick();
            }}
          >
            로그아웃
          </LogOut>
        </HeaderLeft>
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
            <p>
              연결 거절모드 토글 (현재 연결: {currentConnections}/{maxConnections})
            </p>
          </ToggleContainer>
        </HeaderRight>
      </AdvHeader>
      <AdvBody onScroll={handleScroll}>
        <LeftSection>
          <LeftTopSection>
            <CustomerVideo streamManager={activeSubscriber || null} />
          </LeftTopSection>
          <LeftMiddleBarRef ref={leftmiddlebarRef} />
          <LeftBottomSection>
            <CustomerWaiting
              connectedKiosks={connectedKiosks}
              subscribers={subscribers}
              onDisconnect={handleCustomerDisconnect}
              onSetActiveKiosk={handleSetActiveKiosk}
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
            makeButton={true}
          ></Toast>
        )}
      </AdvBody>
      <Modal isOpen={showModal} onRequestClose={handleRejectMeeting} contentLabel="상담 요청">
        <h2>상담 요청이 왔습니다</h2>
        <p>수락하시겠습니까?</p>
        <button onClick={handleAcceptMeeting}>수락</button>
        <button onClick={handleRejectMeeting}>거절</button>
      </Modal>
    </AdvMainPageWrapper>
  );
};

export default AdvMainPage;
