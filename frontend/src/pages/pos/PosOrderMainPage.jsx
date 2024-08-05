import React, { useState } from 'react';
import OrderHistory from '../../components/pos/OrderHistory';
import OrderHistoryDetail from '../../components/pos/OrderHistoryDetail';

function PosOrderMainPage() {
  // 선택된 날짜를 관리하는 상태
  const [selectedDate, setSelectedDate] = useState(new Date());
  // 주문 목록을 관리하는 상태
  const [orders, setOrders] = useState([]);
  // 선택된 주문을 관리하는 상태
  const [selectedOrder, setSelectedOrder] = useState(null);

  // 날짜가 변경될 때 호출되는 함수
  const handleDateChange = (date) => {
    setSelectedDate(date);
    // TODO: 여기에서 새로운 날짜에 대한 주문 내역을 가져와야 합니다.
  };

  // 주문이 선택될 때 호출되는 함수
  const handleOrderSelect = (order) => {
    setSelectedOrder(order);
  };

  // 모달을 닫을 때 호출되는 함수
  const handleCloseModal = () => {
    setSelectedOrder(null);
  };

  // 주문을 취소할 때 호출되는 함수
  const handleCancelOrder = (orderId) => {
    // TODO: 여기에 주문 취소 로직을 추가해야 합니다.
    console.log(`주문 ${orderId} 취소됨`);
  };

  return (
    <div className="PosOrderMainPage">
      {/* 주문 내역 컴포넌트 */}
      <OrderHistory
        date={selectedDate}
        onDateChange={handleDateChange}
        orders={orders}
        onOrderSelect={handleOrderSelect}
      />
      {/* 선택된 주문이 있을 때만 주문 상세 모달을 표시 */}
      {selectedOrder && (
        <OrderHistoryDetail
          order={selectedOrder}
          onCancelOrder={handleCancelOrder}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
}

export default PosOrderMainPage;
