import React from 'react';

function OrderDetail({ order }) {
    return (
        <div className="OrderDetail">
            <h2>주문 상세</h2>
            <p>주문 번호: {order.id}</p>
            <p>총 금액: {order.totalAmount}원</p>
            <h3>주문 내역</h3>
            <ul>
                {order.items.map(item => (
                    <li key={item.id}>
                        {item.name} - {item.quantity}개 - {item.price}원
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default OrderDetail;