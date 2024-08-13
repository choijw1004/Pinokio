import React from 'react';
import styled from 'styled-components';
import OpenViduVideoComponent from './OpenViduVideoComponent';

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
  console.log('CustomerKiosk rendered with streamManager:', streamManager);
  return (
    <CustomerKioskContainer>
      <VideoContainer>
        {streamManager ? (
          <OpenViduVideoComponent streamManager={streamManager} />
        ) : (
          <p>화면 공유가 시작되지 않았습니다.</p>
        )}
      </VideoContainer>
    </CustomerKioskContainer>
  );
}

export default CustomerKiosk;
