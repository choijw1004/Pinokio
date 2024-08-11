import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import ToggleButton from '../common/Toggle';

const Modal = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Input = styled.input`
  width: 100%;
  margin-bottom: 10px;
`;

const TextArea = styled.textarea`
  width: 100%;
  margin-bottom: 10px;
`;

const ProductModal = ({ product, categories, onSave, onClose }) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [image, setImage] = useState('');
  const [detail, setDetail] = useState('');
  const [isScreen, setIsScreen] = useState(false);
  const [isSoldout, setIsSoldout] = useState(false);
  const [category, setCategory] = useState('');

  useEffect(() => {
    if (product) {
      setName(product.name);
      setPrice(product.price.toString());
      setImage(product.image);
      setDetail(product.detail);
      setIsScreen(product.isScreen);
      setIsSoldout(product.isSoldout);
      setCategory(product.category || '');
    }
  }, [product]);

  const handleSave = () => {
    if (!name.trim() || !detail.trim() || !price || price === '0') {
      alert('필수 항목을 모두 입력해주세요.');
      return;
    }
    onSave({
      id: product?.id,
      name,
      price: parseInt(price),
      image,
      detail,
      isScreen,
      isSoldout,
      category,
    });
  };

  return (
    <Modal>
      <Input value={name} onChange={(e) => setName(e.target.value)} placeholder="상품명" required />
      <Input
        value={price}
        onChange={(e) => setPrice(e.target.value)}
        placeholder="가격"
        type="number"
        required
      />
      <Input value={image} onChange={(e) => setImage(e.target.value)} placeholder="이미지 URL" />
      <TextArea
        value={detail}
        onChange={(e) => setDetail(e.target.value)}
        placeholder="상품 설명"
        required
      />
      <select value={category} onChange={(e) => setCategory(e.target.value)}>
        <option value="">카테고리 선택</option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.name}>
            {cat.name}
          </option>
        ))}
      </select>
      <div>
        <span>키오스크 노출</span>
        <ToggleButton toggled={isScreen} onClick={() => setIsScreen(!isScreen)} />
      </div>
      <div>
        <span>품절</span>
        <ToggleButton toggled={isSoldout} onClick={() => setIsSoldout(!isSoldout)} />
      </div>
      <button onClick={handleSave}>확인</button>
      <button onClick={onClose}>취소</button>
    </Modal>
  );
};

export default ProductModal;
