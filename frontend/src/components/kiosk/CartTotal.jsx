import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Button from '../common/Button';

const CT = styled.div`
  display: flex;
  flex-direction: column;
  width: 30%;
`;
const CTtop = styled.div`
  display: flex;
  flex-direction: column;
  height: 9rem;
  padding-left: 1rem;
  padding-top: 1rem;
`;
const GoPaymentButton = styled.div`
  background-color: #7392ff;
  color: white;
  height: 3rem;
  text-align: center;
  line-height: 3rem;
  font-size: 1.3rem;
`;

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

  const changePriceForm = (price) => {
    return '₩' + price.toLocaleString();
  };

  const goPayment = () => {
    // 결제 버튼을 누르면 다음 페이지로 이동
    navigate('/kiosk/payment');
  };
  return (
    <CT>
      <CTtop>
        <text>결제 금액</text>
        <text>{changePriceForm(totalPrice)}</text>
      </CTtop>
      <GoPaymentButton onClick={goPayment}>결제하기</GoPaymentButton>
    </CT>
  );
}

export default CartTotal;
