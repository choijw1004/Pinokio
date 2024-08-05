import React, { useEffect, useState } from "react";
import MenuCategoryCard from "./MenuCategoryCard";
import styled from "styled-components";

const MC = styled.div`
  display: flex;
  flex-direction: row;
`;

function MenuCategory({ categories, selectedCategory, setSelectedCategory }) {
  return (
    <MC>
      {categories.map((cat, index) => (
        <MenuCategoryCard
          key={index}
          cat={cat}
          setselectedcat={setSelectedCategory}
          selectedcat={selectedCategory}
        />
      ))}
    </MC>
  );
}

export default MenuCategory;
