import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled, { keyframes } from 'styled-components';
import { Link as ScrollLink, animateScroll as scroll } from 'react-scroll';
import LOGO from '../components/common/Logo';
import sensor from '../assets/images/main/sensor.png';
import advisor from '../assets/images/main/advisor.png';
import face from '../assets/images/main/face.jfif';

const MainContainer = styled.div`
  font-family: 'goorm-sans-code';
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: relative; /* 사이드바 위치를 위한 상대 위치 지정 */
`;

const Link = styled.a`
  padding: 1% 3% 0 0;
  font-size: 32px;
  font-weight: bold;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s ease;
  color: ivory;

  &:hover {
    color: #b06ab3;
  }
`;

const LogoContainer = styled.div`
  padding: 1% 0 0 3%; /* LOGO에 패딩 추가 */
`;

const Header = styled.div`
  background: linear-gradient(45deg, #b06ab3, #7392ff);
  width: 100%;
  height: 100vh;
`;

const HeaderNav = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const HeaderBody = styled.div`
  padding: 10px;
`;

const BodyHeader = styled.div`
  margin-top: 10%;
  font-weight: bold;
  text-align: center;
`;

const BodyContent = styled.div`
  font-family: 'goorm-sans-medium';
  margin-top: 20px;
  text-align: center;
  font-size: 50px;
`;

const BodySecondContent = styled.div`
  margin-top: 20px;
  text-align: center;
`;

const FirstFunction = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  background: linear-gradient(45deg, #757f9a, #d7dde8);

  .content {
    width: 40%;
  }

  .picture {
    margin-left: 5%;
    width: 50%;
  }

  img {
    width: 80%;
    margin-top: 25%;
  }
`;

const SecondFunction = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  background: linear-gradient(45deg, #f2994a, #f2c94c);

  .content {
    width: 40%;
  }

  .picture {
    margin-left: 5%;
    width: 50%;
  }

  img {
    width: 80%;
    margin-top: 25%;
  }
`;

const ThirdFunction = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  background: linear-gradient(45deg, #1c92d2, #f2fcfe);

  .content {
    width: 40%;
  }

  .picture {
    margin-left: 5%;
    width: 50%;
  }

  img {
    width: 80%;
    margin-top: 25%;
  }
`;

// 애니메이션 키프레임 정의
const float = keyframes`
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
`;

const LearnMoreContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 50px;
`;

const LearnMore = styled(ScrollLink)`
  font-size: 24px;
  font-weight: bold;
  color: ivory;
  cursor: pointer;
  text-decoration: none;
  margin-top: 220px;

  &:hover {
    color: #ff6e7f;
  }
`;

const ArrowDown = styled.div`
  margin-top: 10px;
  font-size: 24px;
  color: ivory;
  animation: ${float} 2s infinite; /* 두둥실 애니메이션 */
  text-align: center;
`;

const Sidebar = styled.div`
  position: fixed;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const SidebarDot = styled(ScrollLink)`
  width: 10px;
  height: 10px;
  background-color: white;
  border-radius: 50%;
  position: relative;
  border: 1px solid light grey;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover::after {
    content: attr(data-label);
    position: absolute;
    right: 25px; /* 점에서 떨어진 위치 */
    top: 50%;
    transform: translateY(-50%);
    background-color: lightsteelblue;
    color: white;
    padding: 5px 10px;
    border-radius: 4px;
    white-space: nowrap;
    opacity: 1;
    transition: opacity 0.3s ease;
  }

  &:hover {
    background-color: #ff6e7f;
  }
`;

function MainPage() {
  const navigate = useNavigate();

  const goLogin = () => {
    navigate('/login');
  };

  return (
    <MainContainer>
      <Header id="main">
        <HeaderNav>
          <LogoContainer>
            <LOGO />
          </LogoContainer>
          <Link onClick={goLogin}>시작하기</Link>
        </HeaderNav>
        <HeaderBody>
          <BodyHeader>|고령층을 위한 키오스크 프로젝트|</BodyHeader>
          <BodyContent>
            어려운 조작 ? <span>피노키오가 !</span>
          </BodyContent>
          <BodySecondContent>
            키오스크 사용에 불편함을 느끼실 어르신들을 위해 피노키오 프로젝트를 진행했습니다.
            <p>아래로 이동해서 핵심기능들을 만나보시죠 !</p>
          </BodySecondContent>
          <LearnMoreContainer>
            <LearnMore to="firstFunction" smooth={true} duration={500}>
              Learn More !
            </LearnMore>
          </LearnMoreContainer>
          <ArrowDown>v</ArrowDown>
        </HeaderBody>
      </Header>
      <FirstFunction id="firstFunction">
        <div className="content">
          <h2>고객 인식</h2>
          <h3>고객 근접 시 거리 센서를 이용해서 서비스를 자동 활성화 시킵니다.</h3>
        </div>
        <div className="picture">
          <img src={sensor} alt="image.png" />
        </div>
      </FirstFunction>
      <SecondFunction id="secondFunction">
        <div className="picture">
          <img src={face} alt="image.png" />
        </div>
        <div className="content">
          <h2>얼굴 식별</h2>
          <h3>고객님의 얼굴을 식별 후 노년층인지 식별하고, 회원 / 비회원 유무를 식별합니다.</h3>
        </div>
      </SecondFunction>
      <ThirdFunction id="thirdFunction">
        <div className="content">
          <h2>화상 상담</h2>
          <h3>1 : 3 화상 상담을 지원해 한명의 상담원이 여러 고객을 상담할 수 있게 지원합니다.</h3>
        </div>
        <div className="picture">
          <img src={advisor} alt="image.png" />
        </div>
      </ThirdFunction>
      <Sidebar>
        <SidebarDot to="main" smooth={true} duration={500} data-label="Home" />
        <SidebarDot to="firstFunction" smooth={true} duration={500} data-label="센서 인식" />
        <SidebarDot to="secondFunction" smooth={true} duration={500} data-label="얼굴 식별" />
        <SidebarDot to="thirdFunction" smooth={true} duration={500} data-label="화상 상담" />
      </Sidebar>
    </MainContainer>
  );
}

export default MainPage;
