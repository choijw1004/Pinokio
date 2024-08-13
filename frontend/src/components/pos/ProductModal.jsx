import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import ToggleButton from '../common/Toggle';
import { postItem } from '../../apis/Item';

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

const ProductModal = ({ product, categories, onClose }) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [amount, setAmount] = useState('');
  const [image, setImage] = useState(null);
  const [detail, setDetail] = useState('');
  const [isScreen, setIsScreen] = useState(false);
  const [isSoldout, setIsSoldout] = useState(false);
  const [category, setCategory] = useState('');
  useEffect(() => {
    if (product !== null) {
      console.log(product);
      setName(product.name);
      setPrice(product.price);
      setAmount(product.amount);
      setImage(product.image);
      setDetail(product.detail);
      console.log(categories.filter((category) => category.id === product.categoryId));
      const categoryName = categories.filter((category) => category.id === product.categoryId)[0]
        .name;
      setCategory(categoryName);
      setIsScreen(product.isScreen);
      setIsSoldout(product.isSoldout);
    }
  }, []);
  const handleSave = async () => {
    if (!name.trim() || !detail.trim() || !price || price === '0') {
      alert('필수 항목을 모두 입력해주세요.');
      return;
    }

    const categoryId = categories.find((cat) => cat.name === category)?.id;

    const itemRequest = {
      categoryId,
      name,
      price: parseInt(price),
      amount: parseInt(amount),
      detail,
      // isScreen,
      // isSoldout,
    };

    const formData = new FormData();
    formData.append(
      'itemRequest',
      new Blob([JSON.stringify(itemRequest)], { type: 'application/json' })
    );
    if (image) {
      formData.append('file', image);
      console.log(image);
    } else {
      // URL을 Blob 객체로 변환하여 FormData에 추가
      const blob = new Blob([], { type: 'image/jpeg' }); // 빈 Blob 객체 생성
      const fileName = ''; // 파일 이름 설정
      formData.append('file', blob, fileName); // Blob 객체와 파일 이름을 함께 추가
      console.log(blob); // 이걸로 확인 가능
    }

    try {
      console.log(itemRequest);
      console.log(image);
      await postItem(formData);
      onClose(); // Close the modal and trigger screen update
    } catch (error) {
      console.error('Error adding product:', error);
    }
  };

  const handleFileChange = (e) => {
    setImage(e.target.files[0]);
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
      <Input
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        placeholder="재고"
        type="number"
        required
      />
      <Input type="file" onChange={handleFileChange} />
      <TextArea
        value={detail}
        onChange={(e) => setDetail(e.target.value)}
        placeholder="상품 설명"
        required
      />
      <select value={category} onChange={(e) => setCategory(e.target.value)}>
        <option value="" disabled>
          카테고리 선택
        </option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.name}>
            {cat.name}
          </option>
        ))}
      </select>
      <div>
        <span>키오스크 노출</span>
        <ToggleButton value={isScreen} setValue={setIsScreen} />
      </div>
      <div>
        <span>품절</span>
        <ToggleButton value={isSoldout} setValue={setIsSoldout} />
      </div>
      <button onClick={handleSave}>확인</button>
      <button onClick={onClose}>취소</button>
    </Modal>
  );
};

export default ProductModal;
