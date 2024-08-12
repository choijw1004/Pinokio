import React, { useState, useEffect, useCallback } from 'react';
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
  resetAdvisor,
} from '../../features/advisor/AdvisorSlice';
import { OpenVidu } from 'openvidu-browser';
Modal.setAppElement('#root');

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

const ToggleContainer = styled.div`
  display: flex;
`;

const AdvMainPage = () => {
  const advisorData = useSelector((state) => state.advisor);
  const userData = useSelector((state) => state.user);
  const { isAvailable, currentConnections, roomToken, roomId, connectedKiosks, maxConnections } =
    advisorData;
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);
  const [consultationRequest, setConsultationRequest] = useState(null);
  const { sendMessage, lastMessage, isConnected, connect } = useWebSocket(userData.token);

  const [OV, setOV] = useState(null);
  const [session, setSession] = useState(null);
  const [publisher, setPublisher] = useState(null);
  const [subscribers, setSubscribers] = useState([]);
  const [isInitialized, setIsInitialized] = useState(false);
  const [activeKiosk, setActiveKiosk] = useState(null);

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
      if (!activeKiosk) {
        setActiveKiosk(connectionId);
      }
    },
    [dispatch, setActiveKiosk]
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
    [userData.email, dispatch, handleCustomerDisconnect, handleCustomerConnect]
  );

  useEffect(() => {
    setSubscribers((prevSubscribers) => {
      const newSubscribers = prevSubscribers.filter((sub) => {
        const connectionId = sub.stream.connection.connectionId;
        return connectedKiosks.some(
          (kiosk) => kiosk.connectionId === connectionId && kiosk.status === 'connected'
        );
      });
      return newSubscribers;
    });
  }, [connectedKiosks]);

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
    if (lastMessage) {
      const data = JSON.parse(lastMessage.data);
      console.log('WebSocket 메시지 수신:', data);
      if (data.type === 'consultationRequest') {
        handleConsultationRequest(data);
      }
    }
  }, [lastMessage]);

  useEffect(() => {
    console.log('Updated connectedKiosks:', connectedKiosks);
  }, [connectedKiosks]);

  useEffect(() => {
    const updatedKiosk = connectedKiosks.find(
      (kiosk) => kiosk.status === 'connected' && kiosk.connectionId && kiosk.kioskId
    );
    if (updatedKiosk) {
      console.log(
        `Kiosk ${updatedKiosk.kioskId} connected with connectionId ${updatedKiosk.connectionId}`
      );
      if (!activeKiosk) {
        setActiveKiosk(updatedKiosk.connectionId);
      }
    }
  }, [connectedKiosks, activeKiosk]);

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

  return (
    <AdvMainPageWrapper>
      <AdvHeader>
        <Logo size={'2.5rem'} />
        <HeaderRight>
          <ToggleContainer>
            <Toggle
              value={!isAvailable}
              setValue={(value) => dispatch(setAvailability(value))}
              size={'5rem'}
            />
            <p>
              연결 거절모드 토글 (현재 연결: {currentConnections}/{maxConnections})
            </p>
          </ToggleContainer>
        </HeaderRight>
      </AdvHeader>
      <AdvBody>
        <LeftSection>
          <LeftTopSection>
            <CustomerVideo
              streamManager={
                subscribers.find((sub) => sub.stream.connection.connectionId === activeKiosk) ||
                null
              }
            />
          </LeftTopSection>
          <LeftBottomSection>
            <CustomerWaiting
              connectedKiosks={connectedKiosks}
              subscribers={subscribers}
              onDisconnect={handleCustomerDisconnect}
              activeKiosk={activeKiosk}
            />
          </LeftBottomSection>
        </LeftSection>
        <RightSection>
          <CustomerKiosk />
        </RightSection>
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
