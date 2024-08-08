import React, { useState, useEffect } from 'react';
import { createCategory } from '../../apis/Category';
import styled from 'styled-components';

const Modal = styled.div`
  ${'' /* 모달 스타일 */}
`;

const Input = styled.input`
  ${'' /* 입력 필드 스타일 */}
`;

const CategoryModal = ({ category, onSave, onClose }) => {
  const [name, setName] = useState('');

  useEffect(() => {
    if (category) {
      setName(category.name);
    }
  }, [category]);

  const handleSave = async (e) => {
    if (!name.trim()) {
      alert('카테고리 이름을 입력해주세요.');
      return;
    }
    console.log(e);
    await createCategory(name);
    onSave({ id: category?.id, name });
    onClose();
  };

  return (
    <Modal>
      <Input
        value={name}
        onChange={(e) => setName(e.target.value)}
        placeholder="카테고리명"
        required
      />
      <button onClick={handleSave}>확인</button>
      <button onClick={onClose}>취소</button>
    </Modal>
  );
};

export default CategoryModal;
