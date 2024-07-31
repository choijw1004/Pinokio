import React, { useState } from 'react';
import styled from 'styled-components';
import Logo from '../../components/common/LOGO';
import { Navigate, useNavigate } from 'react-router-dom';

const LoginWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f4f9;
  padding: 20px;
`;

const FindPasswordForm = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  width: 100%;
  max-width: 500px;
  box-sizing: border-box;
`;

const InputWrapper = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
`;
const Input = styled.input`
  width: 68%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
  color: #333;
  &:focus {
    outline: 1px solid #7392ff;
  }
`;
const StyledButton = styled.button`
  width: 30%;
  padding: 10px;
  margin: 10px 0;
  background-color: #7392ff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  color: white;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  &:hover {
    transform: translateY(-2px);
  }
  &:active {
    background-color: #d8ff75;
  }
`;
function FindPassword1() {
  const navigate = useNavigate();

  const [id, setId] = useState('');
  const [clickedSendEmailButton, setClickedSendEmailButton] = useState(false);
  const [certificationNumber, setCertificationNumber] = useState('');

  const sendEmail = () => {
    setClickedSendEmailButton(true);
    sendCertificationCode();
    // navigate('/findpassword2');
  };

  const sendCertificationCode = () => {};
  return (
    <LoginWrapper>
      <Logo />
      <FindPasswordForm>
        <h2>비밀번호 찾기</h2>
        <InputWrapper>
          <Input
            type="text"
            className="Id"
            placeholder="아이디"
            onChange={(e) => setId(e.target.value)}
          />
          <StyledButton onClick={sendEmail}>이메일 전송</StyledButton>
        </InputWrapper>
        {clickedSendEmailButton ? (
          <InputWrapper>
            <Input
              type="text"
              className="certification-number"
              placeholder="인증번호"
              onChange={(e) => setCertificationNumber(e.target.value)}
            />
            <StyledButton onClick={sendCertificationCode}>인증번호 재전송</StyledButton>
          </InputWrapper>
        ) : null}
        <StyledButton onClick={() => navigate('/findPassword2')}>다음</StyledButton>
      </FindPasswordForm>
    </LoginWrapper>
  );
}

export default FindPassword1;
