import React from 'react';
import styled from 'styled-components';

const LogoStyle = styled.div`
  font-size: 40px;
  color: #7392ff;
  font-family: 'Alfa Slab One', serif;
  font-weight: 400;
  font-style: normal;
`;

function LOGO({ onClick }) {
  return <LogoStyle onClick={onClick}>Pinokio</LogoStyle>;
}

export default LOGO;
