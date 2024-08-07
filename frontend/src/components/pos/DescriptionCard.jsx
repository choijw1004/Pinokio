import React from 'react';
import styled from 'styled-components';

const Main = styled.div`
  display: flex;
  flex-direction: column;
  width: 500px;
  border: 1px solid;
  height: 300px;
  text-align: center;
  justify-content: center;
`;
const Notice = styled.div`
  color: red;
`;

function DescriptionCard({ title, contents, notice }) {
  return (
    <Main>
      <div>{title}</div>
      <div>{contents}</div>
      <Notice>{notice}</Notice>
    </Main>
  );
}

export default DescriptionCard;
