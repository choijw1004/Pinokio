import React, { useState } from 'react';
import styled from 'styled-components';
import Button from '../common/Button';
import { putOrderStatus } from '../../apis/Order';

const OrderDetailContainer = styled.div`
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  position: relative;
  overflow-y: auto;
  width: 100%;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
`;

const CancelledText = styled.span`
  text-decoration: line-through;
`;

const CancelledStatus = styled.span`
  color: #888;
  font-style: italic;
`;

function OrderHistoryDetail({ selectedOrder }) {
  const [isCancelled, setIsCancelled] = useState(selectedOrder.status === 'cancelled');

  const handleCancelOrder = async () => {
    const res = await putOrderStatus(selectedOrder.orderId);
    console.log(res);
    setIsCancelled(true);
  };

  const handleClose = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  return (
    <OrderDetailContainer>
      <CloseButton onClick={handleClose}>&times;</CloseButton>
      <div>
        <h2>주문 상세</h2>
        <p>
          결제 금액:{' '}
          {isCancelled ? (
            <CancelledText>{selectedOrder.totalAmount}원</CancelledText>
          ) : (
            `${selectedOrder.totalAmount}원`
          )}
        </p>
        <p>결제 시간: {new Date(selectedOrder.orderTime).toLocaleString()}</p>
        {isCancelled && <p>취소 시간: {new Date().toLocaleString()}</p>}

        <p>승인 상태: {isCancelled ? '취소됨' : '결제완료'}</p>
        <div>
          <h3>주문 내역</h3>
          {selectedOrder.items.map((item, index) => (
            <div key={index}>
              <p>
                {item.itemName} {item.quantity}개 {item.price * item.quantity}원
              </p>
            </div>
          ))}
        </div>
        <div>
          <Button text="결제 수단 변경" disabled={isCancelled} />
          {!isCancelled && <Button text="결제 취소" onClick={handleCancelOrder} />}
          {isCancelled && <CancelledStatus>취소됨</CancelledStatus>}
        </div>
      </div>
    </OrderDetailContainer>
  );
}

export default OrderHistoryDetail;
