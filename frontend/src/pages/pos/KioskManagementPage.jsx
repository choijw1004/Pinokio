import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { postRegisterKiosk } from '../../apis/Auth'; // API 함수 import
import { getKiosks, deleteKiosk } from '../../apis/Pos';

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

const KioskManagementPage = () => {
  const [kiosks, setKiosks] = useState([]);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [editingKiosk, setEditingKiosk] = useState(null);
  const [newKiosk, setNewKiosk] = useState({ code: '' });

  const fetchKiosks = async () => {
    try {
      const data = await getKiosks();
      setKiosks(data);
    } catch (error) {
      console.error('키오스크 데이터를 불러오는 데 실패했습니다:', error);
    }
  };

  useEffect(() => {
    fetchKiosks();
  }, []);

  const openModal = (kiosk = null) => {
    if (kiosk) {
      setEditingKiosk({ ...kiosk });
    } else {
      setNewKiosk({ code: '' });
    }
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
    setEditingKiosk(null);
    setNewKiosk({ code: '' });
  };

  const handleAddSubmit = async () => {
    try {
      const response = await postRegisterKiosk(newKiosk.code);
      setKiosks([...kiosks, response]);
      await fetchKiosks(); // Fetch the updated kiosks list after adding a new kiosk
      closeModal();
    } catch (error) {
      console.error('키오스크 등록 실패:', error);
      alert('키오스크 등록에 실패했습니다.');
    }
  };

  const handleDeleteKiosk = async (kioskId) => {
    if (window.confirm('정말로 삭제하시겠습니까?')) {
      try {
        await deleteKiosk(kioskId);
        await fetchKiosks(); // Fetch the updated kiosks list after deleting a kiosk
      } catch (error) {
        console.error('키오스크 삭제 실패:', error);
        alert('키오스크 삭제에 실패했습니다.');
      }
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
              <TableCell>{kiosk.email}</TableCell>
              <TableCell>••••••</TableCell>
              <TableCell>
                <ActionButton color="red" onClick={() => handleDeleteKiosk(kiosk.kioskId)}>
                  삭제
                </ActionButton>
              </TableCell>
            </TableRow>
          ))}
        </tbody>
      </Table>
      <AddButton onClick={() => openModal()}>+</AddButton>
      <Modal isOpen={modalIsOpen} onRequestClose={closeModal} contentLabel="Edit Kiosk">
        <ModalContent>
          <>
            <h2>키오스크 추가</h2>

            <ActionButton onClick={handleAddSubmit}>추가</ActionButton>
          </>

          <ActionButton onClick={closeModal}>취소</ActionButton>
        </ModalContent>
      </Modal>
    </Container>
  );
};

export default KioskManagementPage;
