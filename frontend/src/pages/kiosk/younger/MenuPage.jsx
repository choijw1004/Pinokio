import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import MenuCategory from '../../../components/kiosk/MenuCategory';
import MenuMain from '../../../components/kiosk/MenuMain';
import Cart from '../../../components/kiosk/Cart';
import MenuModal from '../../../components/kiosk/MenuModal';

const MenuPageStyle = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  /* align-items: center; */
`;

function MenuPage() {
  const [categories, setCategories] = useState([
    'NEW',
    '에스프레소',
    '콜드 브루',
    '블론드',
    '디카페인 커피',
    '리저브 에스프레소',
    '리저브 드립',
    '티바나',
    '피지오',
    '리프레셔',
    '블렌디드',
    '브루드 커피',
    '기타',
    '병음료',
  ]);
  const [menus, setMenus] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('NEW');
  const [selectedMenu, setSelectedMenu] = useState('');

  const [cartItems, setCartItems] = useState([]);

  const [modal, setModal] = useState(false);

  useEffect(() => {
    getCategory();
  }, []);

  const getCategory = () => {
    /* axios를 이용하여 category를 가져온다. */
  };

  return (
    <MenuPageStyle>
      <MenuCategory
        categories={categories}
        selectedCategory={selectedCategory}
        setSelectedCategory={setSelectedCategory}
      />
      <MenuMain
        selectedCategory={selectedCategory}
        setSelectedMenu={setSelectedMenu}
        setModal={setModal}
      />
      <Cart cartItems={cartItems} setCartItems={setCartItems} />

      {modal && (
        <MenuModal
          itemName={selectedMenu}
          cartItems={cartItems}
          setCartItems={setCartItems}
          setModal={setModal}
        ></MenuModal>
      )}
    </MenuPageStyle>
  );
}

export default MenuPage;
