import React, { useEffect, useCallback, useState } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import CarouselPage from './CarouselPage';
import MenuPage from './younger/MenuPage';
import PaymentPage from './younger/PaymentPage';
import ReceiptPage from './younger/ReceiptPage';
import ElderMenuPage from './elder/ElderMenuPage';
import ElderPaymentPage from './elder/ElderPaymentPage';
import ElderReceiptPage from './elder/ElderReceiptPage';
import LoadingPage from './elder/LoadingPage';
import styled from 'styled-components';
const KioskForm = styled.div`
  /* background-color: white;
  height: 150vh;*/
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f4f4f9;
`;

const KioskOutline = styled.div`
  background-color: black;
  height: 58rem;
  border-radius: 1rem;
  width: 28.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 1rem;
`;

const KioskInline = styled.div`
  background-color: white;
  /* height: 95vh; */
  height: 47rem;
  max-height: 47rem;
  /* width: 30vw; */
  width: 27rem;
  /* max-height: 35rem; */
  display: flex;
  // flex-direction: column;
  // align-items: center;
`;

const WarningMessage = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 20px 40px;
  border-radius: 8px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  z-index: 1000;
  opacity: 0;
  transition: opacity 0.5s ease-in-out;

  &.visible {
    opacity: 1;
  }
`;

function KioskIndex() {
  const navigate = useNavigate();
  const [timer, setTimer] = useState(null);
  const [warning, setWarning] = useState(false);

  const resetTimer = useCallback(() => {
    // 경고 메시지 숨김
    setWarning(false);

    // 기존 타이머 제거
    if (timer) {
      clearTimeout(timer);
    }

    // 새 타이머 설정: 20초 후에 경고를 표시, 30초 후에 페이지 이동
    const newTimer = setTimeout(() => {
      setWarning(true); // 20초 후 경고 표시

      const navigateTimer = setTimeout(() => {
        navigate('/kiosk'); // 60초 후 페이지 이동
      }, 60000); // 60000ms = 60초

      setTimer(navigateTimer); // 새로운 타이머 설정
    }, 50000); // 20000ms = 50초

    setTimer(newTimer); // 새로운 타이머 설정
  }, [navigate, timer]);

  useEffect(() => {
    // 마운트 시 이벤트 리스너 추가
    const events = ['click', 'mousemove', 'keydown', 'scroll'];

    events.forEach((event) => {
      window.addEventListener(event, resetTimer);
    });

    // 언마운트 시 이벤트 리스너 제거
    return () => {
      events.forEach((event) => {
        window.removeEventListener(event, resetTimer);
      });

      if (timer) {
        clearTimeout(timer);
      }
    };
  }, [resetTimer, timer]);

  return (
    <KioskForm>
      <KioskOutline>
        <KioskInline>
          {warning && (
            <WarningMessage className={warning ? 'visible' : ''}>
              10초 뒤 아무 입력이 없으면 페이지가 이동됩니다.
            </WarningMessage>
          )}
          {/* <div style={{ width: '50%' }}> */}
          <Routes>
            <Route path="/" element={<CarouselPage />} />
            <Route path="menu" element={<MenuPage />} />
            <Route path="payment" element={<PaymentPage />} />
            <Route path="receipt" element={<ReceiptPage />} />
            <Route path="elder-menu" element={<ElderMenuPage />} />
            <Route path="elder-payment" element={<ElderPaymentPage />} />
            <Route path="elder-receipt" element={<ElderReceiptPage />} />
            <Route path="loading" element={<LoadingPage />} />
          </Routes>
          {/* </div> */}
        </KioskInline>
      </KioskOutline>
    </KioskForm>
  );
}

export default KioskIndex;
