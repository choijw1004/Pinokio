import React, { useState, useEffect } from 'react';
import OrderHistory from '../../components/pos/OrderHistory';
import OrderHistoryDetail from '../../components/pos/OrderHistoryDetail';
import styled from 'styled-components';

const ComponentRow = styled.div`
  display: flex;
`;
function OrderListPage() {
  const [selectedOrder, setSelectedOrder] = useState(null);

  // const handleCancelOrder = (orderId) => {
  //   setOrders(
  //     orders.map((order) => (order.id === orderId ? { ...order, status: 'cancelled' } : order))
  //   );
  // };

  return (
    <ComponentRow>
      <OrderHistory selectedOrder={selectedOrder} setSelectedOrder={setSelectedOrder} />
      {selectedOrder && <OrderHistoryDetail selectedOrder={selectedOrder} />}
    </ComponentRow>
  );
}

export default OrderListPage;
