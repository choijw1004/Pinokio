import React, { useState } from "react";
import OrderList from "../../components/pos/OrderList";
import OrderDetail from "../../components/pos/OrderDetail";
import Button from "../../components/common/Button";

function PosMainPage() {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);

  const addNewOrder = () => {
    const newOrder = {
      id: orders.length + 1,
      items: [],
      status: "준비 중",
      totalAmount: 0,
    };
    setOrders([...orders, newOrder]);
  };

  const changeOrderStatus = (orderId, newStatus) => {
    if (newStatus === "완료") {
      setOrders(orders.filter((order) => order.id !== orderId));
    } else {
      setOrders(
        orders.map((order) =>
          order.id === orderId ? { ...order, status: newStatus } : order
        )
      );
    }
  };

  const selectOrder = (order) => {
    setSelectedOrder(order);
  };

  return (
    <div className="PosMainPage">
      <Button onClick={addNewOrder} text="새 주문 추가" />
      <div className="order-container">
        <OrderList
          orders={orders}
          onOrderSelect={selectOrder}
          onOrderComplete={(orderId) => changeOrderStatus(orderId, "완료")}
        />
        {selectedOrder && <OrderDetail order={selectedOrder} />}
      </div>
    </div>
  );
}

export default PosMainPage;
