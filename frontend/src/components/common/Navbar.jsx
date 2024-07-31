import React from 'react';
import { Link } from 'react-router-dom';
import MenuButton from '../../assets/images/MenuButton.png'; // 메뉴 버튼 이미지 경로
import CloseButton from '../../assets/images/CloseButton.png'; // 닫기 버튼 이미지 경로
import KioskNavbar from './Logo';
import styled from 'styled-components';

const NavbarContainer = styled.nav`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  align-items: center;
  padding: 1rem;
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
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
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
  return (
    <NavbarContainer>
      <NavbarToggle
        src={isOpen ? CloseButton : MenuButton}
        alt={isOpen ? '닫기' : '메뉴'}
        onClick={toggleNavbar}
      />
      <NavbarMenu isOpen={isOpen}>
        {items.map((item, index) => (
          <NavbarItem key={index}>
            <NavbarLink to={item.path}>{item.text}</NavbarLink>
          </NavbarItem>
        ))}
      </NavbarMenu>
      <LogoLocation>
        <KioskNavbar />
      </LogoLocation>
    </NavbarContainer>
  );
}

export default Navbar;
