import React from "react";
import styled from "styled-components";
import CartItem from "./CartItem";

const Ct = styled.div`
  display: flex;
  flex-direction: column;
  background: green;
  height: 500px;
  padding: 10px;
`;

function Cart({ cartItems, setCartItems }) {
  return (
    <Ct>
      {cartItems.map((item, index) => (
        <CartItem
          key={index}
          item={item}
          cartItems={cartItems}
          setCartItems={setCartItems}
        />
      ))}
    </Ct>
  );
}

export default Cart;
