import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import ToggleButton from '../common/Toggle';
import { postItem, putItem, itemScreenToggle, itemSoldOutToggle } from '../../apis/Item';

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
  const [isSoldOut, setIsSoldout] = useState(false);
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
      setIsSoldout(product.isSoldOut);
    }
  }, []);
  const handleSave = async () => {
    if (!name.trim() || !detail.trim() || !price || price === '0') {
      alert('필수 항목을 모두 입력해주세요.');
      return;
    }

    const formData = new FormData();

    if (product === null) {
      const categoryId = categories.find((cat) => cat.name === category)?.id;

      const itemRequest = {
        categoryId,
        name,
        price: parseInt(price),
        amount: parseInt(amount),
        detail,
      };

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
    } else {
      const categoryId = categories.find((cat) => cat.name === category)?.id;

      const updateItemRequest = {
        categoryId,
        name,
        price: parseInt(price),
        amount: parseInt(amount),
        detail,
        isScreen,
        isSoldOut,
      };

      formData.append(
        'updateItemRequest ',
        new Blob([JSON.stringify(updateItemRequest)], { type: 'application/json' })
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
    }

    try {
      if (product === null) {
        await postItem(formData);
        onClose(); // Close the modal and trigger screen update
      } else {
        console.log('hello', isScreen, isSoldOut);
        await putItem(product.itemId, formData);
        onClose();
      }
    } catch (error) {
      console.error('Error adding product:', error);
    }
  };

  const handleFileChange = (e) => {
    setImage(e.target.files[0]);
  };

  const handleToggle = async (field) => {
    try {
      let updatedProduct;
      if (field === 'isSoldOut') {
        await itemSoldOutToggle(product.itemId);
        setIsSoldout((prev) => !prev); // 상태 업데이트
        updatedProduct = { ...product, isSoldout: !isSoldOut };
      } else if (field === 'isScreen') {
        await itemScreenToggle(product.itemId);
        setIsScreen((prev) => !prev); // 상태 업데이트
        updatedProduct = { ...product, isScreen: !isScreen };
      }
      console.log(updatedProduct); // 업데이트된 상태 확인
    } catch (error) {
      console.error('Toggle failed:', error);
    }
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
        <ToggleButton
          value={product.isSoldOut === 'YES'}
          setValue={() => handleToggle(product, 'isSoldOut')}
        />
      </div>
      <div>
        <span>품절</span>
        <ToggleButton
          value={product.isScreen === 'YES'}
          setValue={() => handleToggle(product, 'isScreen')}
        />
      </div>
      <button onClick={handleSave}>확인</button>
      <button onClick={onClose}>취소</button>
    </Modal>
  );
};

export default ProductModal;
