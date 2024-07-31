import React from 'react';
import styled from 'styled-components';
import CartItem from './CartItem';
import CartTotal from './CartTotal';

const Ct = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: white;
  border: 1px solid black;
  border-radius: 10px;
  padding: 10px;
  font-family: 'PeoplefirstNeatLoudTTF';
  @font-face {
    font-family: 'PeoplefirstNeatLoudTTF';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2406-2@1.0/PeoplefirstNeatLoudTTF.woff2')
      format('woff2');
    font-weight: normal;
    font-style: normal;
  }
`;

function Cart({ cartItems, setCartItems }) {
  return (
    <Ct>
      <div>
        {cartItems.map((item, index) => (
          <CartItem key={index} item={item} cartItems={cartItems} setCartItems={setCartItems} />
        ))}
      </div>
      <CartTotal cartItems={cartItems} />
    </Ct>
  );
}

export default Cart;
