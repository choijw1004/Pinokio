import React, { useEffect, useRef, useState } from 'react';
import MenuCategoryCard from './MenuCategoryCard';
import styled from 'styled-components';
import rightArrow from '../../assets/images/icons8-원-셰브론-오른쪽-80.png';
import leftArrow from '../../assets/images/icons8-원-셰브론-왼쪽-80.png';

const MC = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  background-color: yellow;
`;

const Arrow = styled.img`
  width: 10%;
`;
const MenuCategoryCards = styled.div`
  width: 80%;
  display: flex;
  flex-direction: row;
`;

function MenuCategory({ categories, selectedCategory, setSelectedCategory }) {
  const [nowFirst, setNowFirst] = useState(0);
  const leftArrow = useRef(null);
  const showSize = 4;
  useEffect(() => {
    leftArrow.current.opacity = '0.5';
  });
  return (
    <MC>
      <Arrow ref={leftArrow} src={leftArrow} onClick={() => setNowFirst(nowFirst - 1)} />
      <MenuCategoryCards>
        {categories.slice(nowFirst, nowFirst + showSize).map((cat, index) => (
          <MenuCategoryCard
            key={index}
            cat={cat}
            setselectedcat={setSelectedCategory}
            selectedcat={selectedCategory}
            showSize={showSize}
          />
        ))}
      </MenuCategoryCards>

      <Arrow src={rightArrow} onClick={() => setNowFirst(nowFirst + 1)} />
    </MC>
  );
}

export default MenuCategory;
