import React from 'react';
import styled from 'styled-components';

const VideoSection = styled.div`
  width: 695px;
  height: 300px;
  border: solid black 1px;
  border-radius: 12px;
  margin-bottom: 10px;
  text-align: center;
`;

function CustomerVideo(props) {
  return (
    <div>
      <VideoSection>
        <p>연결 대기중</p>
        <p>components/advisor/CustomerVideo.jsx</p>
      </VideoSection>
    </div>
  );
}

export default CustomerVideo;
