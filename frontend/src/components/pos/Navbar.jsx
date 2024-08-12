import React from 'react';
import { Link } from 'react-router-dom';
import MenuButton from '../../assets/images/MenuButton.png'; // 메뉴 버튼 이미지 경로
import CloseButton from '../../assets/images/CloseButton.png'; // 닫기 버튼 이미지 경로
import LOGO from '../common/Logo';
import styled from 'styled-components';
import ToggleIcon from './ToggleIcon';

const NavbarContainer = styled.nav`
  width: 100%;
  background-color: white;
  display: flex;
  align-items: center;
  width: 94%;
  height: 4rem;
  margin: 1rem;
  border-radius: 1rem;
  box-shadow: 2px 4px 0 rgba(0, 0, 0, 0.25);
  padding: 0 1rem;
`;

const NavbarToggle = styled.img`
  width: 2rem;
  height: 2rem;
  cursor: pointer;
`;

const NavbarMenu = styled.ul`
  margin: 0;
  margin-left: 2vw;
  padding: 0;
  position: fixed;
  top: 6rem;
  left: ${(props) => (props.isOpen ? '0' : '-21vw')};
  width: 18vw;
  height: 85%;
  border-radius: 1vw;
  background-color: white;
  box-shadow: 2px 4px rgb(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
  transition: left 0.3s ease-in-out;
`;

const NavbarItem = styled.li`
  margin: 0.5rem 1rem;
`;

const NavbarLink = styled(Link)`
  text-decoration: none;
  color: black;
`;

const LogoLocation = styled.div`
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
`;

function Navbar({ isOpen, toggleNavbar }) {
  // 메뉴 아이템 클릭 시 NavbarMenu를 닫기 위한 핸들러
  const handleItemClick = () => {
    if (isOpen) {
      toggleNavbar();
    }
  };

  const items = [
    { path: '/pos', text: '주문 홈' },
    { path: '/pos/order-list', text: '주문 내역' },
    { path: '/pos/kiosk-management', text: '키오스크 관리' },
    { path: '/pos/product-management', text: '상품 관리' },
    { path: '/pos/sales-report', text: '매출 리포트' },
  ];

  return (
    <NavbarContainer>
      <ToggleIcon isOn={isOpen} setIsOn={toggleNavbar} />
      <NavbarMenu isOpen={isOpen}>
        {items.map((item, index) => (
          <NavbarItem key={index} onClick={handleItemClick}>
            <NavbarLink to={item.path}>{item.text}</NavbarLink>
          </NavbarItem>
        ))}
      </NavbarMenu>
      <LogoLocation>
        <LOGO />
      </LogoLocation>
    </NavbarContainer>
  );
}

export default Navbar;
