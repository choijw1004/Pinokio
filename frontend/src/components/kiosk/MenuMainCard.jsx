import React from "react";
import styled from "styled-components";

const MMC = styled.div`
  background: white;
  width: 100px;
  border: black solid 1px;
`;

function MenuMainCard({ menu, setSelectedMenu, setModal }) {
  const handleClick = () => {
    setModal(true);
    setSelectedMenu(menu);
  };

  return <MMC onClick={handleClick}>{menu}</MMC>;
}

export default MenuMainCard;
