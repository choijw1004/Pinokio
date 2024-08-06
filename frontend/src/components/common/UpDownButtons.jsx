import React, { useState } from 'react';
import styled from 'styled-components';

const DownButton = styled.div`
  width: ${(props) => props.size};
  height: ${(props) => props.size};
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${(props) => props.color};
`;

const MenuCount = styled.div`
  width: ${(props) => props.size};
  height: ${(props) => props.size};
  display: flex;
  justify-content: center;
  align-items: center;
`;

const UpButton = styled.div`
  width: ${(props) => props.size};
  height: ${(props) => props.size};
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${(props) => props.color};
`;

UpDownButtons.defaultProps = {
  // 기본 색, 사이즈 설정
  color: '#7392ff',
  size: '2rem',
};

function UpDownButtons({ value, setValue, size, color }) {
  return (
    <UpDownButtons>
      <DownButton
        onClick={() => (value > 0 ? setValue(value - 1) : null)}
        size={size}
        color={color}
      >
        -
      </DownButton>
      <MenuCount>{value}</MenuCount>
      <UpButton onClick={() => setValue(value + 1)} size={size} color={color}>
        +
      </UpButton>
    </UpDownButtons>
  );
}

export default UpDownButtons;
