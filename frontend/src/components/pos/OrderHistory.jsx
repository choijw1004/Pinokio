import React, { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import styled from 'styled-components';
import RangeDatePicker from './RangeDatePicker';
import { getOrdersByRange } from '../../apis/Order';

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
  background-color: #4575f3;
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

const OrderList = styled.div``;

const OrderListEach = styled.div`
  border: 1px solid #ccc;
  height: 100px;
  padding: 10px 10px 0 10px;
  background-color: ${(props) => (props.isSelected ? '#ffd8ca' : 'white')};
  /* &:hover {
    background-color: #ffd8ca;
  } */
`;

const Menu = styled.div`
  margin-top: 15px;
`;

const OrderStatus = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
`;

function OrderHistory({ selectedOrder, setSelectedOrder }) {
  const [orders, setOrders] = useState(null);
  const [dateRange, setDateRange] = useState([new Date(), new Date()]);
  const [startDate, endDate] = dateRange;

  useEffect(() => {
    // console.log('dateRange is changed');
    getOrders();
    // setSelectedOrder(orders[0]);
  }, [startDate, endDate]);

  const getOrders = async () => {
    // console.log(startDate, endDate);
    const orders_temp = await getOrdersByRange(makeDateFormat(startDate), makeDateFormat(endDate));
    console.log('order list : ', orders_temp);
    setOrders(orders_temp);

    setSelectedOrder(orders_temp[0]);
  };

  const makeDateFormat = (date) => {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const dateStr = `${year}-${month}-${day}`;
    // 어떤 날짜여도 'YYYY-DD-YY'형식으로 변환!
    return dateStr;
  };

  return (
    <OrderHistoryContainer>
      <Header>결제 내역</Header>
      <div className="date-picker">
        <RangeDatePicker setDateRange={setDateRange} />
      </div>
      <OrderList>
        {orders &&
          orders.map((order) => (
            <OrderListEach
              key={order.orderId}
              onClick={() => setSelectedOrder(order)}
              isSelected={order.orderId === selectedOrder.orderId}
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
                    <div>주문번호 #{order.orderId}</div>
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
                    {order.items.map((item) => `${item.itemName} ${item.quantity}개`).join(', ')}{' '}
                  </Menu>
                  <OrderStatus>
                    <div>주문시간 # {new Date(order.orderTime).toLocaleString()}</div>
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
