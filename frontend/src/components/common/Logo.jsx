import React from 'react';
import styled from 'styled-components';

const LogoStyle = styled.div`
  font-size: ${(props) => props.size};
  color: ${(props) => (props.color ? props.color : '#7392ff')};
  font-family: 'Alfa Slab One';
  font-weight: 400;
  font-style: normal;
`;

Logo.defaultProps = {
  size: '3rem',
};

function Logo({ onClick, size, color }) {
  return (
    <LogoStyle onClick={onClick} size={size} color={color}>
      Pinokio
    </LogoStyle>
  );
}

export default Logo;
