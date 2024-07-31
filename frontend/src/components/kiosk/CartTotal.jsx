import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Button from '../common/Button';

const CT = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  background: #eeeeee;
  height: 70px;
  margin: 5px;
`;

const StyledButton = styled.div``;

function CartTotal({ cartItems }) {
  const navigate = useNavigate();
  const [totalPrice, setTotalPrice] = useState(0);
  useEffect(() => {
    calculatePrice();
  }, [cartItems]);

  const calculatePrice = () => {
    let ttl = 0;
    for (var i = 0; i < cartItems.length; i++) {
      console.log(cartItems);
      ttl += cartItems[i].itemPrice * cartItems[i].itemCount;
    }
    setTotalPrice(ttl);
  };

  const goPayment = () => {
    // 결제 버튼을 누르면 다음 페이지로 이동
    navigate('/kiosk/payment');
  };
  return (
    <CT>
      <h3>결제 금액</h3>
      <h4>{totalPrice}</h4>
      <Button onClick={goPayment} text="결제" />
    </CT>
  );
}

export default CartTotal;
