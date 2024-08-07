import React from 'react';
import styled from 'styled-components';

const LogoStyle = styled.div`
  font-size: ${(props) => props.size};
  color: #7392ff;
  font-family: 'Alfa Slab One';
  font-weight: 400;
  font-style: normal;
`;

Logo.defaultProps = {
  size: '3rem',
};

function Logo({ onClick, size }) {
  return (
    <LogoStyle onClick={onClick} size={size}>
      Pinokio
    </LogoStyle>
  );
}

export default Logo;
