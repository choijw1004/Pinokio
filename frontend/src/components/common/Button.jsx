import React from 'react';
import styled from 'styled-components';

const StyledButton = styled.button`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
`;

function Button(props) {
  return (
    <StyledButton
      onClick={props.onClick}
      type={props.type}
      width={props.width}
      height={props.height}
    >
      {props.text}
    </StyledButton>
  );
}

export default Button;
