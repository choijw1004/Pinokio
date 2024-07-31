import React from 'react';
import styled from 'styled-components';

const CI = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  background: white;
  height: 30px;
  margin: 5px;
  font-size: 30px;
`;

const CartItemRight = styled.div`
  display: flex;
  flex-direction: row;
`;

const CountButtonsContainer = styled.div``;

const CountUpButton = styled.div``;

const CountDownButton = styled.div``;

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
      <text>{item.itemName}</text>
      <CartItemRight>
        <CountButtonsContainer>
          <CountUpButton onClick={() => changeCount(+1)}>+</CountUpButton>
          <text>{item.itemCount}</text>
          <CountDownButton onClick={() => changeCount(-1)}>-</CountDownButton>
        </CountButtonsContainer>
        <button onClick={() => changeCount(-1 * item.itemCount)}>del</button>
        <text>{item.itemPrice}</text>
      </CartItemRight>
    </CI>
  );
}

export default CartItem;
