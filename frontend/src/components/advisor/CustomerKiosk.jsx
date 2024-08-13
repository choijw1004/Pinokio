import React from 'react';
import styled from 'styled-components';
import OpenViduVideoComponent from './OpenViduComponent';

const CustomerKioskContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const VideoContainer = styled.div`
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;
`;

function CustomerKiosk({ streamManager }) {
  let streamType = 'unknown';
  if (streamManager) {
    try {
      const connectionData = streamManager.stream.connection.data;
      const [clientData] = connectionData.split('%/%');
      const parsedClientData = JSON.parse(clientData);
      streamType = parsedClientData.clientData;
    } catch (error) {
      console.error('Error processing connection data:', error);
    }
  }

  return (
    <CustomerKioskContainer>
      <VideoContainer>
        {streamManager && streamType === 'screen' ? (
          <OpenViduVideoComponent streamManager={streamManager} />
        ) : (
          <p>화면 공유가 시작되지 않았습니다.</p>
        )}
      </VideoContainer>
    </CustomerKioskContainer>
  );
}

export default CustomerKiosk;
