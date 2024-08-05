import React from "react";
import styled from "styled-components";

const CI = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  background: white;
  height: 70px;
  margin: 5px;
`;

const CartItemRight = styled.div`
  display: flex;
  flex-direction: row;
`;

function CartItem({ item, cartItems, setCartItems }) {
  const changeCount = (count) => {
    for (var i = 0; i < cartItems.length; i++) {
      if (cartItems[i].itemName === item.itemName) {
        const updatedCartItems = [...cartItems];
        updatedCartItems[i].itemCount += count;
        if (updatedCartItems[i].itemCount <= 0) {
          updatedCartItems.splice(i, 1);
        }
        setCartItems(updatedCartItems);
        return;
      }
    }
  };

  return (
    <CI>
      <h3>{item.itemName}</h3>
      <CartItemRight>
        <button onClick={() => changeCount(+1)}>+</button>
        <h5>{item.itemCount}</h5>
        <button onClick={() => changeCount(-1)}>-</button>
        <button onClick={() => changeCount(-1 * item.itemCount)}>del</button>
      </CartItemRight>
    </CI>
  );
}

export default CartItem;
