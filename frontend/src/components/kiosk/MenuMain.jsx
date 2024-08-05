import React, { useEffect, useState } from "react";
import MenuMainCard from "./MenuMainCard";
import styled from "styled-components";

const MM = styled.div`
  display: flex;
  flex-direction: row;
`;

function MenuMain({ selectedCategory, setSelectedMenu, setModal }) {
  const [menus, setMenus] = useState([]);

  useEffect(() => {
    /* 처움 렌더링할 때랑 카테고리 선택할 때마다 getMenu 실행 */
    getMenu();
    console.log("change menus");
  }, [selectedCategory]);

  const getMenu = () => {
    /* selectedCategory를 이용하여 해당되는 메뉴 가져오기 */

    /* 아래는 임시 코드(추후 axios로 변경 예정) */
    const list = [];
    for (var i = 0; i < 10; i++) {
      list.push(selectedCategory + (i + 1));
    }
    setMenus(list);
  };

  return (
    <MM>
      this is menu main
      {menus.map((menu, index) => (
        <MenuMainCard
          key={index}
          menu={menu}
          setSelectedMenu={setSelectedMenu}
          setModal={setModal}
        />
      ))}
    </MM>
  );
}

export default MenuMain;
