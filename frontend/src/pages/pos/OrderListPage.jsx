import React, { useState, useEffect } from 'react';
import OrderHistory from '../../components/pos/OrderHistory';
import OrderHistoryDetail from '../../components/pos/OrderHistoryDetail';
import styled from 'styled-components';

const ComponentRow = styled.div`
  display: flex;
`;
function OrderListPage() {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);

  useEffect(() => {
    // 여기서 선택된 날짜에 따라 주문 내역을 가져오는 API 호출
    const dummyOrders = [
      {
        id: 1,
        paymentMethod: '카드',
        items: [{ name: '아메리카노', quantity: 2, price: 4500 }],
        totalAmount: 9000,
        status: 'completed',
        orderTime: new Date().toISOString(),
      },
      {
        id: 2,
        paymentMethod: '현금',
        items: [{ name: '아이스티', quantity: 1, price: 8000 }],
        totalAmount: 8000,
        status: 'cancelled',
        orderTime: new Date(Date.now() - 86400000).toISOString(), // 1일 전
      },
      {
        id: 3,
        paymentMethod: '카드',
        items: [{ name: '치즈케이크', quantity: 1, price: 7500 }],
        totalAmount: 7500,
        status: 'completed',
        orderTime: new Date(Date.now() - 172800000).toISOString(), // 2일 전
      },
      {
        id: 4,
        paymentMethod: '현금',
        items: [{ name: '카페라떼', quantity: 2, price: 10000 }],
        totalAmount: 10000,
        status: 'completed',
        orderTime: new Date(Date.now() - 259200000).toISOString(), // 3일 전
      },
      {
        id: 5,
        paymentMethod: '카드',
        items: [{ name: '카페라떼', quantity: 1, price: 5000 }],
        totalAmount: 5000,
        status: 'completed',
        orderTime: new Date(Date.now() - 345600000).toISOString(), // 4일 전
      },
    ];
    setOrders(dummyOrders);
  }, [selectedDate]);

  const handleDateChange = (date) => {
    setSelectedDate(date);
  };

  const handleOrderSelect = (order) => {
    setSelectedOrder(order);
  };

  const handleCancelOrder = (orderId) => {
    setOrders(
      orders.map((order) => (order.id === orderId ? { ...order, status: 'cancelled' } : order))
    );
  };

  return (
    <ComponentRow>
      <OrderHistory
        date={selectedDate}
        onDateChange={handleDateChange}
        orders={orders}
        onOrderSelect={handleOrderSelect}
      />
      {selectedOrder && (
        <OrderHistoryDetail
          order={selectedOrder}
          onCancelOrder={handleCancelOrder}
          onClose={() => setSelectedOrder(null)}
        />
      )}
    </ComponentRow>
  );
}

export default OrderListPage;
