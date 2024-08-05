import React from 'react';
import { Link } from 'react-router-dom';

function Navbar({ items, isOpen, toggleNavbar }) {
  return (
    <nav className={`Navbar ${isOpen ? 'open' : ''}`}>
      <button onClick={toggleNavbar}>
        {isOpen ? '닫기' : '메뉴'}
      </button>
      {isOpen && (
        <ul>
          {items.map((item, index) => (
            <li key={index}>
              <Link to={item.path}>{item.text}</Link>
            </li>
          ))}
        </ul>
      )}
    </nav>
  );
}

export default Navbar;