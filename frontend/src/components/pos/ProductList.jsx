import React, { useState } from 'react';
import styled from 'styled-components';
import ToggleButton from '../common/Toggle';
import { deleteItem } from '../../apis/Item'; // Import the deleteItem function

const ProductTable = styled.table`
  width: 100%;
  border-collapse: collapse;
`;

const ProductRow = styled.tr`
  border-bottom: 1px solid #ddd;
  &:hover .delete-button {
    opacity: 1;
  }
`;

const ProductCell = styled.td`
  padding: 10px;
  vertical-align: middle;
`;

const DeleteCell = styled(ProductCell)`
  width: 30px; // 고정 너비 설정
`;

const ProductImage = styled.img`
  width: 50px;
  height: 50px;
  object-fit: cover;
`;

const DeleteButton = styled.button`
  opacity: 0;
  background: none;
  border: none;
  color: red;
  cursor: pointer;
  transition: opacity 0.3s ease;
`;

const ModalBackground = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const ModalButton = styled.button`
  margin: 0 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
`;

const ProductList = ({ products, onEdit, onDelete, onToggle, setToastMessage }) => {
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [productToDelete, setProductToDelete] = useState(null);

  const handleDeleteClick = (product) => {
    setProductToDelete(product);
    setShowConfirmModal(true);
  };

  const confirmDelete = async () => {
    try {
      await deleteItem(productToDelete.itemId); // Call the deleteItem function
      onDelete(productToDelete.itemId); // Call the onDelete handler to remove from list
      setToastMessage(`${productToDelete.name} 상품 삭제 완료!`);
    } catch (error) {
      setToastMessage('상품 삭제 실패!');
    }
    setShowConfirmModal(false); // Close the modal after delete
  };

  const handleToggle = (product, field) => {
    const updatedProduct = { ...product, [field]: product[field] === 'YES' ? 'NO' : 'YES' };
    onToggle(updatedProduct);
  };

  return (
    <>
      <ProductTable>
        <thead>
          <ProductRow>
            <th></th>
            <th>이미지</th>
            <th>상품명</th>
            <th>가격</th>
            <th>재고</th>
            <th>품절 여부</th>
            <th>키오스크 노출</th>
          </ProductRow>
        </thead>
        <tbody>
          {products.map((product) => (
            <ProductRow key={product.id}>
              <DeleteCell>
                <DeleteButton className="delete-button" onClick={() => handleDeleteClick(product)}>
                  X
                </DeleteButton>
              </DeleteCell>
              <ProductCell>
                <ProductImage src={product.file} alt={product.name} />
              </ProductCell>
              <ProductCell onClick={() => onEdit(product)}>{product.name}</ProductCell>
              <ProductCell>{product.price.toLocaleString()} 원</ProductCell>
              <ProductCell>{product.amount.toLocaleString()} 개</ProductCell>
              <ProductCell>
                <ToggleButton
                  value={product.isSoldOut === 'YES'}
                  setValue={() => handleToggle(product, 'isSoldOut')}
                />
              </ProductCell>
              <ProductCell>
                <ToggleButton
                  value={product.isScreen === 'YES'}
                  setValue={() => handleToggle(product, 'isScreen')}
                />
              </ProductCell>
            </ProductRow>
          ))}
        </tbody>
      </ProductTable>
      {showConfirmModal && (
        <ModalBackground>
          <ModalContent>
            <p>정말 {productToDelete?.name} 상품을 삭제하시겠습니까?</p>
            <ModalButton onClick={confirmDelete}>확인</ModalButton>
            <ModalButton onClick={() => setShowConfirmModal(false)}>취소</ModalButton>
          </ModalContent>
        </ModalBackground>
      )}
    </>
  );
};

export default ProductList;
