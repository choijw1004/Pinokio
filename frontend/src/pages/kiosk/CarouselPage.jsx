import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import carouselimage from '../../assets/images/carouselimage.jpg';
import carouselimage2 from '../../assets/images/carouselimage2.jpg';
import carouselimage3 from '../../assets/images/carouselimage3.jpg';

const CarouselPageStyle = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
`;

const ButtonContainer = styled.div`
  position: absolute;
  display: flex;
  width: 100%;
  justify-content: center;
  margin-top: 30rem;
`;

const CarouselWindow = styled.div`
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 30vw;
  height: 1000vh;
`;

const CarouselButton = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  border-radius: 10px;
  height: 12vh;
  width: 8vw;
  margin: 0 0.5vw;
  font-weight: Bold;
  font-size: 3vh;
`;

const Slider = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const CarouselImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const CarouselMessage = styled.div`
  position: absolute;

  color: white;
  margin-top: 20vh;
`;

function CarouselPage() {
  const navigate = useNavigate();
  const slideRef = useRef(0);

  const [result, setResult] = useState(null);
  const [carouselIndex, setCarouselIndex] = useState(0);

  const [carouselimages, setCarouselimages] = useState([
    carouselimage,
    carouselimage2,
    carouselimage3,
    carouselimage,
  ]);

  // useEffect(() => {
  //   /* 여기서부터 SSE 관련 코드 */
  //   // SSE 연결 생성
  //   const eventSource = new EventSource('http://localhost:8080/api/face-recognition-events');

  //   // 서버로부터 메시지를 받았을 때 실행될 핸들러
  //   eventSource.onmessage = (event) => {
  //     const data = JSON.parse(event.data);
  //     console.log('Received SSE data:', data);
  //     // 받은 데이터로 상태 업데이트
  //     setResult(data);
  //   };

  //   // 에러 발생 시 실행될 핸들러
  //   eventSource.onerror = (error) => {
  //     console.error('SSE error:', error);
  //     eventSource.close();
  //   };

  //   return () => {
  //     // 컴포넌트 언마운트 시 SSE 연결 종료
  //     eventSource.close();
  //   };
  // });

  // 결과가 없을 때 표시할 내용

  const havingHere = () => {
    navigate('/kiosk/menu', { state: { where: 'having here' } });
  };

  const takeAway = () => {
    navigate('/kiosk/menu', { state: { where: 'take away' } });
  };

  // const slideAutoPlay = () => {
  //   const interval = setInterval(() => {
  //     handleSwipe(1);

  //     if (carouselIndex === $slider.children.length - 1) {
  //       setTimeout(() => {
  //         $slider.style.transition = 'none';
  //         currentIndex = 1;
  //         moveOffset = (100 / slideAmount) * currentIndex;
  //         $slider.style.transform = `translateX(-${moveOffset}%)`;
  //       }, slideSpeed);
  //     }
  //   }, 3000);
  // };

  useEffect(() => {
    moveSlider();
  }, [carouselIndex]);

  const moveSlider = () => {
    setTimeout(() => {
      setCarouselIndex(carouselIndex + 1);
      // console.log('carouselsize : ', carouselimages.length);
      console.log(slideRef);
      if (carouselIndex % 2 === 1) {
        // 홀수일 때 넘기고
        slideRef.current.style.transform = `translateX(-${13.5 * (carouselIndex + 1)}rem)`;
        slideRef.current.style.transition = 'all 0.5s ease-in-out';
        // console.log(carouselIndex);
      } else if (
        // 끝에 도달하고, 짝수일 때 style을 바꾼다.
        carouselIndex >= 2 * (carouselimages.length - 1) &&
        carouselIndex % (2 * (carouselimages.length - 1)) === 0
      ) {
        slideRef.current.style.transform = 'none';
        slideRef.current.style.transition = `none`;
        setCarouselIndex(1);
      }
    }, 2000);
  };

  return (
    <CarouselPageStyle>
      <Slider ref={slideRef}>
        {carouselimages &&
          carouselimages.map((image, key) => <CarouselImage src={image} key={key} alt="" />)}
      </Slider>
      <CarouselWindow>
        {result ? (
          <div>
            <h2>얼굴 인식 결과:</h2>
            <p>회원 여부: {result.is_member ? '회원' : '비회원'}</p>
            <p>나이: {result.age}</p>
            <p>성별: {result.gender}</p>
            {result.is_member && result.member_info && (
              <div>
                <h3>회원 정보:</h3>
                <p>ID: {result.member_info.id}</p>
                <p>등록된 나이: {result.member_info.age}</p>
                <p>등록된 성별: {result.member_info.gender}</p>
              </div>
            )}
          </div>
        ) : (
          <CarouselMessage>얼굴 인식 결과를 기다리는 중...</CarouselMessage>
        )}
        <ButtonContainer>
          <CarouselButton onClick={havingHere}>
            <img src="" alt="" />
            매장
          </CarouselButton>
          <CarouselButton onClick={takeAway}>
            <img src="" alt="" />
            포장
          </CarouselButton>
        </ButtonContainer>
      </CarouselWindow>
    </CarouselPageStyle>
  );
}

export default CarouselPage;
