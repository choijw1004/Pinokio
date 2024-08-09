import React, { useEffect } from 'react';
import styled from 'styled-components';
import coffeeImage from '../../assets/images/coffee_image_rb.png';

const MMC = styled.div`
  display: flex;
  flex-direction: column;
  background: white;
  border: 1px solid #c7c7c7;
  margin: 0.5vh 0.2vw;
  width: 6.5rem;
  opacity: ${(props) => (props.isSoldOut === 'YES' ? '0.2' : '1')};
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
  useEffect(() => {}, []);

  const handleClick = () => {
    if (menu.isSoldOut === 'NO') {
      setModal(true);
      setSelectedMenu(menu);
    } else {
      setModal(false);
    }
  };

  return (
    <MMC onClick={handleClick} isSoldOut={menu.isSoldOut}>
      <Image src={coffeeImage} />
      <MenuContents>
        <MenuName>{menu.name}</MenuName>
        <MenuNameEng>{menu.detail}</MenuNameEng>
        <MenuPrice>{menu.price}</MenuPrice>
      </MenuContents>
    </MMC>
  );
}

export default MenuMainCard;
