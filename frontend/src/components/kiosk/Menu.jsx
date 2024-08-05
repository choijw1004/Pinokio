import React, { useEffect, useRef, useState } from "react";
import MenuCategory from "./MenuCategory";
import MenuMain from "./MenuMain";
import Cart from "./Cart";
import { useNavigate } from "react-router-dom";
import Button from "../common/Button";
import MenuModal from "./MenuModal";

function Menu() {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([
    "NEW",
    "에스프레소",
    "콜드 브루",
    "블론드",
    "디카페인 커피",
    "리저브 에스프레소",
    "리저브 드립",
    "티바나",
    "피지오",
    "리프레셔",
    "블렌디드",
    "브루드 커피",
    "기타",
    "병음료",
  ]);
  const [menus, setMenus] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("NEW");
  const [selectedMenu, setSelectedMenu] = useState("");

  const [cartItems, setCartItems] = useState([]);

  const [modal, setModal] = useState(false);

  useEffect(() => {
    getCategory();
  }, []);

  const getCategory = () => {
    /* axios를 이용하여 category를 가져온다. */
  };

  const goPayment = () => {
    // 결제 버튼을 누르면 다음 페이지로 이동
    navigate("/kiosk/payment");
  };

  return (
    <div className="Menu">
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
      <Button onClick={goPayment} text="결제" />
      {modal && (
        <MenuModal
          itemName={selectedMenu}
          cartItems={cartItems}
          setCartItems={setCartItems}
          setModal={setModal}
        ></MenuModal>
      )}
    </div>
  );
}

export default Menu;
