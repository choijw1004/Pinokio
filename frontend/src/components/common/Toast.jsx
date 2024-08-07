import React, { useEffect } from 'react';
import styled from 'styled-components';

const ToastContainer = styled.div`
  position: fixed;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  bottom: 20px;
  right: 20px;
  background-color: #333;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  opacity: 0.9;
`;
const ToastButton = styled.div`
  background-color: ${(props) => props.color};
  width: 50px;
  border-radius: 50%;
`;

const Toast = ({ message, setAnswer }) => {
  // useEffect(() => {
  //   const timer = setTimeout(() => {
  //     onClose();
  //   }, 3000);

  //   return () => clearTimeout(timer);
  // }, [onClose]);

  return (
    <ToastContainer>
      {message}
      <ToastButton color="rgb(67, 57, 255, 0.8)" onClick={() => setAnswer('accept')}>
        수락
      </ToastButton>
      <ToastButton color="rgb(236, 115, 72, 0.8)" onClick={() => setAnswer('reject')}>
        거절
      </ToastButton>
    </ToastContainer>
  );
};

export default Toast;
