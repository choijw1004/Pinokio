import React, { useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import MenuCategory from '../../../components/kiosk/MenuCategory';
import MenuMain from '../../../components/kiosk/MenuMain';
import Cart from '../../../components/kiosk/Cart';
import MenuModal from '../../../components/kiosk/modal/MenuModal';
// import MenuData from '../../../data/MenuData.json';
import { useSelector } from 'react-redux';
import { getCategories } from '../../../apis/Category';
import { getItemsByCategoryId } from '../../../apis/Item';

const MenuPageStyle = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #efefef;
  min-width: 27rem;
`;

const KioskHeader = styled.div`
  border-bottom: 1px #d9d9d9 solid;
  background-color: white;
  width: 100%;
  height: 5rem;
`;

const Logo = styled.div`
  font-size: 3vh;
  color: #7392ff;
  font-family: 'Alfa Slab One', serif;
  font-weight: 400;
  font-style: normal;
  padding-left: 1vw;
  padding-top: 1vh;
`;

const KioskBody = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: scroll;
  height: calc(30rem - 2px);
  &::-webkit-scrollbar {
    display: none;
  }
`;

function MenuPage() {
  const [categories, setCategories] = useState([]);
  const categoriesMounted = useRef(false);

  const [menus, setMenus] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const selectedCategoryMounted = useRef(false);
  const [selectedMenu, setSelectedMenu] = useState(null);

  const [cartItems, setCartItems] = useState([]);

  const userData = useSelector((store) => store.user);

  const [modal, setModal] = useState(false);

  useEffect(() => {
    console.log('first rendering');
    getCategory();

    // 처음 렌더링 했을 때 순서 getCategory ->  useEffect(categories) -> useEffect(selectedCategory)
  }, []);

  const getCategory = async () => {
    /* axios를 이용하여 category를 가져온다. */
    const category_data = await getCategories(userData.typeInfo.posId);
    console.log('received categories datas : ', category_data);
    setCategories(category_data.responseList);
  };

  useEffect(() => {
    if (!categoriesMounted.current) {
      console.log('categories mounted : ');
      categoriesMounted.current = true;
    } else {
      console.log('categories updated');
      console.log(categories);
      setSelectedCategory(categories[0]);
    }
  }, [categories]);

  useEffect(() => {
    if (!selectedCategoryMounted.current) {
      console.log('selectedCategory mounted : ');
      selectedCategoryMounted.current = true;
    } else {
      console.log('selectedCategory updated');
      console.log(selectedCategory);
      getMenu();
    }
  }, [selectedCategory]);

  const getMenu = async () => {
    if (selectedCategory && userData) {
      const menu_data = await getItemsByCategoryId(selectedCategory.id);
      console.log('received menus datas : ', menu_data);
      // 화면에 보여줄 데이터만 필터링 (isScreen이 YES인 경우)
      const filteredMenus = menu_data.responseList
        .filter((menu) => menu.isScreen === 'YES')
        .map((menu) => {
          menu['count'] = 0; // count 초기화
          return menu;
        });
      setMenus(filteredMenus);
    }
  };

  return (
    <MenuPageStyle>
      <KioskHeader>
        <Logo>Pinokio</Logo>
        {categories.length > 0 && selectedCategory && (
          <MenuCategory
            categories={categories}
            selectedCategory={selectedCategory}
            setSelectedCategory={setSelectedCategory}
          />
        )}
      </KioskHeader>
      <KioskBody>
        {menus.length > 0 && selectedCategory && (
          <MenuMain
            selectedCategory={selectedCategory}
            setSelectedMenu={setSelectedMenu}
            setModal={setModal}
            menus={menus}
          />
        )}
      </KioskBody>
      <Cart cartItems={cartItems} setCartItems={setCartItems} isElder={false} />

      {modal && (
        <MenuModal
          item={selectedMenu}
          cartItems={cartItems}
          setCartItems={setCartItems}
          setModal={setModal}
          isElder={false}
        ></MenuModal>
      )}
    </MenuPageStyle>
  );
}

export default MenuPage;
