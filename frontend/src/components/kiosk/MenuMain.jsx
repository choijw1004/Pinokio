import React, { useEffect, useState } from 'react';
import MenuMainCard from './MenuMainCard';
import styled from 'styled-components';

const MM = styled.div`
  display: flex;
  flex-direction: column;
  width: 80%;
  padding-top: 2vh;
`;

function MenuMain({ selectedCategory, setSelectedMenu, setModal }) {
  // 한 줄에 showSize 개만큼 보여줌
  const [showSize, setShowSize] = useState(3);
  // 메뉴들을 페이지에 맞게 담고 있는 배열
  const [pages, setPages] = useState([]);

  useEffect(() => {
    /* 처움 렌더링할 때랑 카테고리 선택할 때마다 getMenu 실행 */
    getMenu();
  }, [selectedCategory]);

  const makeList = () => {};

  const getMenu = () => {
    /* selectedCategory를 이용하여 해당되는 메뉴 가져오기 */

    /* 아래는 임시 코드(추후 axios로 변경 예정) */
    const list = [];
    for (var i = 0; i < 10; i++) {
      list.push(selectedCategory + (i + 1));
    }

    const pagesLength = list.length / showSize;
    let pages_temp = [];
    for (let p = 0; p < pagesLength; p++) {
      pages_temp.push([]);
      for (let i = 0; i < showSize; i++) {
        pages_temp[p].push(list[p * pagesLength * showSize + i]);
      }
    }
    // console.log(pages_temp);

    setPages(pages_temp);
    // console.log(pages);
  };

  return (
    <MM>
      {pages &&
        pages.map((row, rowIndex) => (
          <div key={rowIndex} style={{ display: 'flex', flexDirection: 'row' }}>
            {row.map((menu, colIndex) => (
              <MenuMainCard
                key={colIndex}
                menu={menu}
                setSelectedMenu={setSelectedMenu}
                setModal={setModal}
              />
            ))}
          </div>
        ))}
    </MM>
  );
}

export default MenuMain;
