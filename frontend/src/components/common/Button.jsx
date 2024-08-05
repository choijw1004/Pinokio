import React from 'react';
import styled from 'styled-components';

const StyledButton = styled.button`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  // padding: 10px;
  // margin: 10px 0;
  // background-color: rgb(102, 102, 255);
  // border: none;
  // border-radius: 4px;
  // font-size: 30px;
  // color: white;
  // cursor: pointer;
  // font-family: 'PeoplefirstNeatLoudTTF';

  // &:hover {
  //   background-color: #add8e6;
  // }
  // @font-face {
  //   font-family: 'PeoplefirstNeatLoudTTF';
  //   src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2406-2@1.0/PeoplefirstNeatLoudTTF.woff2')
  //     format('woff2');
  //   font-weight: normal;
  //   font-style: normal;
  // }
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
