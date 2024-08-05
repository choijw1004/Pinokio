import React from "react";
import styled from "styled-components";

const MCC = styled.div`
  background: ${(props) =>
    props.cat == props.selectedcat ? "orange" : "yellow"};
  width: 100px;
  border: black solid 1px;
`;

function MenuCategoryCard({ cat, setselectedcat, selectedcat }) {
  const handleClick = () => {
    setselectedcat(cat);
  };

  return (
    <MCC onClick={handleClick} selectedcat={selectedcat} cat={cat}>
      {cat}
    </MCC>
  );
}

export default MenuCategoryCard;
