import React from 'react';
import { Link } from 'react-router-dom';
import MenuButton from '../../assets/images/MenuButton.png'; // 메뉴 버튼 이미지 경로
import CloseButton from '../../assets/images/CloseButton.png'; // 닫기 버튼 이미지 경로
import LOGO from './Logo';
import styled from 'styled-components';

const NavbarContainer = styled.nav`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: white;
  z-index: 1000;
  display: flex;
  align-items: center;
  padding: 1rem;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
`;

const NavbarToggle = styled.img`
  width: 24px;
  height: 24px;
  cursor: pointer;
`;

const NavbarMenu = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
  position: fixed;
  top: 60px;
  left: ${(props) => (props.isOpen ? '0' : '-250px')};
  width: 250px;
  height: 100%;
  background-color: white;
  box-shadow: 2px 0 0 rgba(0, 0, 0, 0.1);
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

function Navbar({ items, isOpen, toggleNavbar }) {
  // 메뉴 아이템 클릭 시 NavbarMenu를 닫기 위한 핸들러
  const handleItemClick = () => {
    if (isOpen) {
      toggleNavbar();
    }
  };

  return (
    <NavbarContainer>
      <NavbarToggle
        src={isOpen ? CloseButton : MenuButton}
        alt={isOpen ? '닫기' : '메뉴'}
        onClick={toggleNavbar}
      />
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
