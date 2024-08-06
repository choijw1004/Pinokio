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

const UpDownButtonsStyle = styled.div`
  --height: ${(props) => props.size};
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: calc(var(--height) * 3);
  height: var(--height);
  border: calc(var(--height) / 60) solid #d9d9d9;
  border-radius: 0.1rem;
`;

// const DownButton = styled.div`
//   width: 20px;
//   height: 20px;
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   color: black;
// `;

const MenuCount = styled.div`
  width: ${(props) => props.size};
  height: ${(props) => props.size};
  display: flex;
  justify-content: center;
  align-items: center;
`;
// const MenuCount = styled.div`
//   width: 20px;
//   height: 20px;
//   display: flex;
//   justify-content: center;
//   align-items: center;
// `;

const UpButton = styled.div`
  width: ${(props) => props.size};
  height: ${(props) => props.size};
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${(props) => props.color};
`;

// const UpButton = styled.div`
//   width: 20px;
//   height: 20px;
//   justify-content: center;
//   align-items: center;
//   color: black;
// `;

// UpDownButtons.defaultProps = {
//   // 기본 색, 사이즈 설정
//   color: '#7392ff',
//   size: '2rem',
// };

function UpDownButtons({ value, setValue, size, color }) {
  return (
    <UpDownButtonsStyle>
      <DownButton
        onClick={() => (value > 0 ? setValue(value - 1) : null)}
        size={size}
        color={color}
      >
        -
      </DownButton>
      <MenuCount size={size}>{value}</MenuCount>
      <UpButton onClick={() => setValue(value + 1)} size={size} color={color}>
        +
      </UpButton>
    </UpDownButtonsStyle>
  );
}

export default UpDownButtons;
