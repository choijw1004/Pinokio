import React from 'react';
import styled from 'styled-components';

const ToggleWrapper = styled.button`
  width: 50px;
  height: 25px;
  border-radius: 15px;
  border: none;
  cursor: pointer;
  background-color: ${(props) => (props.toggled ? '#4CAF50' : '#ccc')};
  position: relative;
  transition: background-color 0.3s ease;
`;

const Slider = styled.span`
  position: absolute;
  top: 2px;
  left: ${(props) => (props.toggled ? '27px' : '2px')};
  width: 21px;
  height: 21px;
  border-radius: 50%;
  background-color: white;
  transition: left 0.3s ease;
`;

const ToggleButton = ({ toggled, onClick }) => (
  <ToggleWrapper toggled={toggled} onClick={onClick}>
    <Slider toggled={toggled} />
  </ToggleWrapper>
);

export default ToggleButton;
