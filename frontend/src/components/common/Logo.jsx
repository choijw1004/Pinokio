import React from 'react';
import styled from 'styled-components';

const Logo = styled.div`
  font-size: 40px;
  color: #7392ff;
  font-family: 'Alfa Slab One', serif;
  font-weight: 400;
  font-style: normal;
`;

function LOGO({ onClick }) {
  return <Logo onClick={onClick}>Pinokio</Logo>;
}

export default LOGO;
