import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import KioskMain from '../assets/images/main/kioskmain.png';
import KioskSenior from '../assets/images/main/kiosksenior.png';
import PosMain from '../assets/images/main/posmain.png';
import LOGO from '../components/common/Logo';

const MainContainer = styled.div`
  width: 100%;
  background-color: #071739;
`;

const LinksContainer = styled.div`
  width: 100%;
  display: flex;
  padding: 10px;
  gap: 10px;
  justify-content: space-between;
  background-color: white;
`;
const Links = styled.div`
  margin-top: 13px;
  margin-right: 20px;
`;
const Link = styled.a`
  font-size: 16px; /* 텍스트 크기 */
  text-decoration: none; /* 밑줄 제거 */
  cursor: pointer; /* 커서 포인터로 변경 */
  transition: color 0.3s ease; /* 애니메이션 효과 */
  margin-left: 30px;

  &:hover {
    color: #0056b3; /* 호버 시 텍스트 색상 변경 */
  }
`;

const Carousel = styled.div`
  position: relative;
  overflow: hidden;
  margin-top: 30px;
  height: 800px;
`;

const CarouselTrack = styled.div`
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

  .kioskmain {
    position: absolute;
    right: 300px;
    top: 30px;
    width: 300px;
    height: 500px;
  }

  .kiosksenior {
    position: absolute;
    right: 300px;
    top: 30px;
    width: 300px;
    height: 500px;
  }

  .posmain {
    position: absolute;
    right: 300px;
    top: 30px;
    width: 500px;
    height: 300px;
  }

  h2 {
    color: white;
    margin-left: 200px;
  }

  p {
    color: white;
    margin-left: 200px;
  }
`;

const CarouselButton = styled.button`
  position: absolute;
  top: 30%;
  transform: translateY(-50%);
  background-color: rgba(255, 255, 255, 0.7);
  border: none;
  padding: 10px;
  cursor: pointer;
  font-size: 24px;

  &.prev {
    z-index: 1;
    left: 10px;
  }

  &.next {
    right: 10px;
  }
`;

const DiscriptionBox = styled.div`
  background-color: #4b6382;
  height: 200px;
  width: 100%;
  padding: 10px;

  h3 {
    margin-top: 50px;
    margin-left: 190px;
  }

  h4 {
    margin-top: 50px;
    margin-left: 190px;
  }
`;

function MainPage() {
  const navigate = useNavigate();

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
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h4>해당 서비스에 대한 설명입니다.</h4>
      </DiscriptionBox>
      <img className="kioskmain" src={KioskMain} alt="키오스크 메인" />
    </CarouselItem>,
    <CarouselItem key="2">
      <h2>두 번째 아이템</h2>
      <p>두 번째 아이템 설명</p>
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h4>해당 서비스에 대한 설명입니다.</h4>
      </DiscriptionBox>
      <img className="kiosksenior" src={KioskSenior} alt="키오스크 노인" />
    </CarouselItem>,
    <CarouselItem key="3">
      <h2>세 번째 아이템</h2>
      <p>세 번째 아이템 설명</p>
      <DiscriptionBox>
        <h3>어쩌구 저쩌구 엄청난 설명 !</h3>
        <h4>해당 서비스에 대한 설명입니다.</h4>
      </DiscriptionBox>
      <img className="posmain" src={PosMain} alt="포스 메인" />
    </CarouselItem>,
  ];

  const [currentIndex, setCurrentIndex] = useState(0);

  const goToNext = () => {
    if (currentIndex === items.length - 1) {
      setCurrentIndex(0);
    } else {
      setCurrentIndex((prevIndex) => prevIndex + 1);
    }
  };

  const goToPrev = () => {
    if (currentIndex === 0) {
      setCurrentIndex(items.length - 1);
    } else {
      setCurrentIndex((prevIndex) => prevIndex - 1);
    }
  };

  return (
    <MainContainer>
      <LinksContainer>
        <LOGO />
        <Links>
          <Link onClick={goPosLogin}>포스로그인</Link>
          <Link onClick={goKioskLogin}>키오스크로그인</Link>
          <Link onClick={goAdvisorLogin}>상담사로그인</Link>
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
      </Carousel>
    </MainContainer>
  );
}

export default MainPage;
