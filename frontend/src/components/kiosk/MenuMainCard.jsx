import React from 'react';
import styled from 'styled-components';
import coffeeImage from '../../assets/images/coffee_image_rb.png';

const MMC = styled.div`
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 5px;
  border: 1px solid;
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
`;

const MenuContents = styled.div`
  padding-left: 20px;
`;
const MenuName = styled.div`
  font-size: 20px;
`;
const MenuNameEng = styled.div`
  font-size: 10px;
`;
const MenuPrice = styled.div`
  font-size: 15px;
  font-weight: Bold;
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
