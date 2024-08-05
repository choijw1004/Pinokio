import React, { useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';

const Container = styled.div`
  text-align: center;
  padding: 20px;
`;

const Table = styled.table`
  width: 80%;
  margin: 20px auto;
  border-collapse: collapse;
  text-align: left;
`;

const TableRow = styled.tr`
  border-bottom: 1px solid #ddd;
`;

const TableHeader = styled.th`
  padding: 10px;
`;

const TableCell = styled.td`
  padding: 10px;
`;

const ActionButton = styled.button`
  color: ${(props) => props.color || 'black'};
  cursor: pointer;
  background: none;
  border: none;
  padding: 5px;
  margin: 0 5px;
`;

const AddButton = styled.button`
  width: 50px;
  height: 50px;
  font-size: 24px;
  cursor: pointer;
  background-color: #f4f4f9;
  border: 1px solid #ddd;
  border-radius: 50%;
  display: block;
  margin: 20px auto;
`;

const ModalContent = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;

  input {
    margin: 10px 0;
    padding: 10px;
    width: 80%;
    font-size: 16px;
  }
`;

function KioskManagementPage() {
  const [kiosks, setKiosks] = useState([]);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [editingKiosk, setEditingKiosk] = useState(null);
  const [newKiosk, setNewKiosk] = useState({ id: '', password: '' });

  const openModal = (kiosk = null) => {
    if (kiosk) {
      setEditingKiosk({ ...kiosk }); // Create a new object to avoid direct mutation
    } else {
      setNewKiosk({ id: '', password: '' });
    }
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
    setEditingKiosk(null);
    setNewKiosk({ id: '', password: '' });
  };

  const handleEditSubmit = () => {
    setKiosks(kiosks.map((k) => (k.id === editingKiosk.id ? { ...editingKiosk } : k)));
    closeModal();
  };

  const handleAddSubmit = () => {
    if (kiosks.some((kiosk) => kiosk.id === newKiosk.id)) {
      alert('이미 존재하는 아이디입니다.');
      return;
    }
    setKiosks([...kiosks, newKiosk]);
    closeModal();
  };

  const deleteKiosk = (index) => {
    if (window.confirm('정말로 삭제하시겠습니까?')) {
      setKiosks(kiosks.filter((_, i) => i !== index));
    }
  };

  return (
    <Container>
      <h1>키오스크 관리</h1>
      <Table>
        <thead>
          <TableRow>
            <TableHeader>키오스크명</TableHeader>
            <TableHeader>아이디</TableHeader>
            <TableHeader>비밀번호</TableHeader>
          </TableRow>
        </thead>
        <tbody>
          {kiosks.map((kiosk, index) => (
            <TableRow key={index}>
              <TableCell>{index + 1}번 키오스크</TableCell>
              <TableCell>{kiosk.id}</TableCell>
              <TableCell>••••••</TableCell>
              <TableCell>
                <ActionButton color="red" onClick={() => deleteKiosk(index)}>
                  삭제
                </ActionButton>
                <ActionButton color="blue" onClick={() => openModal(kiosk)}>
                  편집
                </ActionButton>
              </TableCell>
            </TableRow>
          ))}
        </tbody>
      </Table>
      <AddButton onClick={() => openModal()}>+</AddButton>
      <Modal isOpen={modalIsOpen} onRequestClose={closeModal} contentLabel="Edit Kiosk">
        <ModalContent>
          {editingKiosk ? (
            <>
              <h2>키오스크 편집</h2>
              <input
                type="text"
                placeholder="아이디"
                value={editingKiosk.id}
                onChange={(e) => setEditingKiosk({ ...editingKiosk, id: e.target.value })}
              />
              <input
                type="password"
                placeholder="비밀번호"
                value={editingKiosk.password}
                onChange={(e) => setEditingKiosk({ ...editingKiosk, password: e.target.value })}
              />
              <ActionButton onClick={handleEditSubmit}>저장</ActionButton>
            </>
          ) : (
            <>
              <h2>키오스크 추가</h2>
              <input
                type="text"
                placeholder="아이디"
                value={newKiosk.id}
                onChange={(e) => setNewKiosk({ ...newKiosk, id: e.target.value })}
              />
              <input
                type="password"
                placeholder="비밀번호"
                value={newKiosk.password}
                onChange={(e) => setNewKiosk({ ...newKiosk, password: e.target.value })}
              />
              <ActionButton onClick={handleAddSubmit}>추가</ActionButton>
            </>
          )}
          <ActionButton onClick={closeModal}>취소</ActionButton>
        </ModalContent>
      </Modal>
    </Container>
  );
}

export default KioskManagementPage;
