import React from 'react';
import styled from 'styled-components';
import Symbol from './Symbol';

const LogoStyle = styled.div`
  font-size: ${(props) => props.size};
  color: #7392ff;
  font-family: 'Alfa Slab One';
  font-weight: 400;
  font-style: normal;
  line-height: ${(props) => `calc(${props.size} * 2)`};
`;

const LogoWithSymbolStyle = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: ${(props) => `calc(${props.size} * 5.5)`};
`;

LogoWithSymbol.defaultProps = {
  size: '3rem',
};

function LogoWithSymbol({ onClick, size }) {
  return (
    <LogoWithSymbolStyle size={size}>
      <Symbol size={size}></Symbol>
      <LogoStyle onClick={onClick} size={size}>
        Pinokio
      </LogoStyle>
    </LogoWithSymbolStyle>
  );
}

export default LogoWithSymbol;
