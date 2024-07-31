import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import styled from 'styled-components';

const OrderHistoryContainer = styled.div`
  display: inline-block;
`;

const CancelledText = styled.div`
  text-decoration: line-through;
`;

const SelectPayment = styled.div`
  display: flex;
  justify-content: space-between;
  font-weight: bolder;
`;

const Header = styled.div`
  height: 30px;
  color: white;
  background-color: #4339ff;
  padding: 5px;
  font-weight: bolder;
  font-size: 24px;
`;

// 날짜 선택기의 입력 필드를 스타일링
const DateInput = styled.input`
  width: 318px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  color: #333;
  &:focus {
    border-color: #007bff;
    outline: none;
  }
`;

const CustomDatePicker = ({ selected, onChange }) => (
  <DatePicker showIcon selected={selected} onChange={onChange} customInput={<DateInput />} />
);

const OrderList = styled.div``;

const OrderListEach = styled.div`
  border: 1px solid #ccc;
  height: 100px;
  padding: 10px 10px 0 10px;
  background-color: ${({ isSelected }) => (isSelected ? '#ffd8ca' : 'white')};
  &:hover {
    background-color: #ffd8ca;
  }
`;

const Menu = styled.div`
  margin-top: 15px;
`;

const OrderStatus = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
`;

function OrderHistory({ date, onDateChange, orders, onOrderSelect }) {
  const [selectedOrderId, setSelectedOrderId] = useState(null);

  const handleOrderClick = (order) => {
    setSelectedOrderId(order.id);
    onOrderSelect(order);
  };

  return (
    <OrderHistoryContainer>
      <Header>결제 내역</Header>
      <div className="date-picker">
        <CustomDatePicker selected={date} onChange={onDateChange} />
      </div>
      <OrderList>
        {orders.map((order) => (
          <OrderListEach
            key={order.id}
            onClick={() => handleOrderClick(order)}
            isSelected={order.id === selectedOrderId}
          >
            {order.status === 'cancelled' ? (
              <>
                <SelectPayment>
                  <CancelledText>{order.paymentMethod}</CancelledText>
                  <CancelledText>{order.totalAmount}원</CancelledText>
                </SelectPayment>
                <Menu>
                  {order.items.map((item) => `${item.name} ${item.quantity}개`).join(', ')}{' '}
                </Menu>
                <OrderStatus>
                  <div>주문번호 #{order.id}</div>
                  <div>취소</div>
                </OrderStatus>
              </>
            ) : (
              <>
                <SelectPayment>
                  <div>{order.paymentMethod}</div>
                  <div>{order.totalAmount}원</div>
                </SelectPayment>
                <Menu>
                  {order.items.map((item) => `${item.name} ${item.quantity}개`).join(', ')}{' '}
                </Menu>
                <OrderStatus>
                  <div>주문번호 #{order.id}</div>
                  <div>결제완료</div>
                </OrderStatus>
              </>
            )}
          </OrderListEach>
        ))}
      </OrderList>
    </OrderHistoryContainer>
  );
}

export default OrderHistory;
