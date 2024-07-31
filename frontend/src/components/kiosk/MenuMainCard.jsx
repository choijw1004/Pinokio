import React from 'react';
import styled from 'styled-components';
import coffeeImage from '../../assets/images/coffee_image_rb.png';

const MMC = styled.div`
  display: flex;
  flex-direction: column;
  background: white;
  border: 1px solid #c7c7c7;
  margin: 0.5vh 0.2vw;
  /* font-family: 'PeoplefirstNeatLoudTTF';
  margin: 10px;
  @font-face {
    font-family: 'PeoplefirstNeatLoudTTF';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2406-2@1.0/PeoplefirstNeatLoudTTF.woff2')
      format('woff2');
    font-weight: normal;
    font-style: normal;
  } */
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const MenuContents = styled.div`
  padding-left: 1vw;
  font-family: var(--font-CafeOhsquareAir);
`;
const MenuName = styled.div`
  font-size: 0.7rem;
`;
const MenuNameEng = styled.div`
  font-size: 0.3rem;
`;
const MenuPrice = styled.div`
  font-size: 1rem;
`;

function MenuMainCard({ menu, setSelectedMenu, setModal }) {
  const handleClick = () => {
    setModal(true);
    setSelectedMenu(menu);
  };

  return (
    <MMC onClick={handleClick}>
      <Image src={coffeeImage} />
      <MenuContents>
        <MenuName>{menu}</MenuName>
        <MenuNameEng>{'Americano'}</MenuNameEng>
        <MenuPrice>{'5,000원'}</MenuPrice>
      </MenuContents>
    </MMC>
  );
}

export default MenuMainCard;
