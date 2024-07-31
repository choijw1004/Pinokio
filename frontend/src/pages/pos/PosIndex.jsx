import React, { useState } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import Navbar from '../../components/common/Navbar';
import PosMainPage from './PosMainPage';
import OrderListPage from './OrderListPage';
import KioskManagementPage from './KioskManagementPage';
import ProductManagementPage from './ProductManagementPage';
import SalesReportPage from './SalesReportPage';
import styled from 'styled-components';
import '../../styles/pos/pos.css';

const Content = styled.div`
  transition: margin-left 0.3s ease-in-out;
`;

function PosIndex() {
  const location = useLocation();
  const [isNavbarOpen, setIsNavbarOpen] = useState(false);

  const navItems = [
    { path: '/pos', text: '주문 홈' },
    { path: '/pos/order-list', text: '주문 내역' },
    { path: '/pos/kiosk-management', text: '키오스크 관리' },
    { path: '/pos/product-management', text: '상품 관리' },
    { path: '/pos/sales-report', text: '매출 리포트' },
  ];

  const toggleNavbar = () => {
    setIsNavbarOpen(!isNavbarOpen);
  };

  return (
    <div className="Pos">
      <Navbar items={navItems} isOpen={isNavbarOpen} toggleNavbar={toggleNavbar} />
      <div style={{ marginTop: 60 }}>
        <Content isNavbarOpen={isNavbarOpen}>
          <Routes>
            <Route path="/" element={<PosMainPage />} />
            <Route path="order-list" element={<OrderListPage />} />
            <Route path="kiosk-management" element={<KioskManagementPage />} />
            <Route path="product-management" element={<ProductManagementPage />} />
            <Route path="sales-report" element={<SalesReportPage />} />
          </Routes>
        </Content>
      </div>
    </div>
  );
}

export default PosIndex;
