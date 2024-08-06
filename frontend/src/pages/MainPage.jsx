import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import KioskMain from '../assets/images/main/kioskmain.png';
import KioskSenior from '../assets/images/main/kiosksenior.png';
import PosMain from '../assets/images/main/posmain.png';
import LOGO from '../components/common/Logo';
import Toggle from '../components/common/Toggle';
import UpDownButtons from '../components/common/UpDownButtons';

const MainContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const LinksContainer = styled.div`
  display: flex;
  gap: 10px;
  justify-content: space-around;
  background-color: white;
  width: 100%;
`;

const LogoContainer = styled.div`
  margin-top: 15px;
  margin-right: 450px;
`;

const Links = styled.div`
  display: flex;
  margin-top: 23px;
  gap: 30px;
`;

const Link = styled.a`
  font-size: 16px;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s ease;

  &:hover {
    color: #0056b3;
  }
`;

const Carousel = styled.div`
  position: relative;
  overflow: hidden;
  margin-top: 20px;
  width: 100%;
  max-width: 1200px;
  height: 600px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
`;

const CarouselTrack = styled.div`
  display: flex;
  transition: transform 0.5s ease-in-out;
`;

const CarouselItemContainer = styled.div`
  display: flex;
  width: 100%;
`;

const CarouselItem = styled.div`
  min-width: 100%;
  box-sizing: border-box;
  position: relative;
  padding: 20px;

  .kioskmain,
  .kiosksenior,
  .posmain {
    position: absolute;
    right: 50px;
    top: 50px;
    max-width: 300px;
    max-height: 500px;
    width: auto;
    height: auto;
  }

  h2 {
    color: #333;
    margin-left: 20px;
  }

  p {
    color: #555;
    margin-left: 20px;
  }
`;

const CarouselButton = styled.button`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  border: none;
  padding: 10px;
  cursor: pointer;
  font-size: 24px;

  &.prev {
    left: 10px;
    z-index: 1;
  }

  &.next {
    right: 10px;
  }
`;

const IndicatorContainer = styled.div`
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
`;

const Indicator = styled.div`
  width: 10px;
  height: 10px;
  background-color: ${({ $isActive }) => ($isActive ? '#0056b3' : '#ccc')};
  border-radius: 50%;
  cursor: pointer;
`;

const DiscriptionBox = styled.div`
  background-color: #ffc1cc;
  height: 200px;
  padding: 10px;
  margin-top: 80px;
  border-radius: 10px;

  h3 {
    margin-top: 20px;
    margin-left: 20px;
  }

  h5 {
    margin-top: 10px;
    margin-left: 20px;
  }
`;

function MainPage() {
  const navigate = useNavigate();
  const [maxAvailable, setMaxAvailable] = useState(1);

  const goPosLogin = () => {
    navigate('/login');
  };
  const goKioskLogin = () => {
    navigate('/login');
  };
  const goAdvisorLogin = () => {
    navigate('/login');
  };

  const items = [
    <CarouselItem key="1">
      <h2>AI 얼굴 인식</h2>
      <p>고객님의 얼굴을 인식해 나이, 성별을 인식해요.</p>
      <p>고객님의 얼굴을 인식해 나이, 성별을 인식해요.</p>
      <p>고객님의 얼굴을 인식해 나이, 성별을 인식해요.</p>
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
      </DiscriptionBox>
      <img className="kioskmain" src={KioskMain} alt="키오스크 메인" />
    </CarouselItem>,
    <CarouselItem key="2">
      <h2>두 번째 아이템</h2>
      <p>두 번째 아이템 설명</p>
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
      </DiscriptionBox>
      <img className="kiosksenior" src={KioskSenior} alt="키오스크 노인" />
    </CarouselItem>,
    <CarouselItem key="3">
      <h2>세 번째 아이템</h2>
      <p>세 번째 아이템 설명</p>
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
        <h5>해당 서비스에 대한 설명입니다.</h5>
      </DiscriptionBox>
      <img className="posmain" src={PosMain} alt="포스 메인" />
    </CarouselItem>,
  ];

  const [currentIndex, setCurrentIndex] = useState(0);

  const goToNext = () => {
    if (currentIndex < items.length - 1) {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const goToPrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
    }
  };

  return (
    <MainContainer>
      <LinksContainer>
        <LogoContainer>
          <LOGO />
          <UpDownButtons
            value={maxAvailable}
            setValue={setMaxAvailable}
            color={'#7392ff'}
            size={'2rem'}
          />
        </LogoContainer>
        <Links>
          <Link onClick={goPosLogin}>PosLogin</Link>
          <Link onClick={goKioskLogin}>KioskLogin</Link>
          <Link onClick={goAdvisorLogin}>AdvisorLogin</Link>
        </Links>
      </LinksContainer>
      <Carousel>
        <CarouselButton className="prev" onClick={goToPrev}>
          &lt;
        </CarouselButton>
        <CarouselTrack style={{ transform: `translateX(-${currentIndex * 100}%)` }}>
          <CarouselItemContainer>{items}</CarouselItemContainer>
        </CarouselTrack>
        <CarouselButton className="next" onClick={goToNext}>
          &gt;
        </CarouselButton>
        <IndicatorContainer>
          {items.map((_, index) => (
            <Indicator
              key={index}
              $isActive={index === currentIndex}
              onClick={() => setCurrentIndex(index)}
            />
          ))}
        </IndicatorContainer>
      </Carousel>
    </MainContainer>
  );
}

export default MainPage;
