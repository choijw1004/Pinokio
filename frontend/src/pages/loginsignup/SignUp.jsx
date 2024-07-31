import React, { useState } from 'react';
import Logo from '../../components/common/LOGO';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const SignUpWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f4f9;
  padding: 20px;
`;

const SignUpForm = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  width: 100%;
  max-width: 600px;
  box-sizing: border-box;

  .has-label {
    margin: 10px 0 0 0;
  }
`;

const SignUpTitle = styled.div`
  color: #7392ff;
  font-size: 25px;
  font-weight: Bold;
  margin: 10px 0 20px 0;
`;
const Input = styled.input`
  width: 100%;
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

const LabelContainer = styled.label`
  width: 100%;
`;

const AddedMessage = styled.div`
  color: ${(props) => (props.isCorrect === true ? 'limegreen' : 'red')};
  font-size: 10px;
  text-align: end;
`;

const StyledButton = styled.button`
  width: 100%;
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

const SelectInput = styled.select`
  width: 100%;
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

function SignUp() {
  const [id, setId] = useState('');
  const [isUsableId, setIsUsableId] = useState(false);
  const [password, setPassword] = useState('');
  const [passwordChecking, setPasswordChecking] = useState('');
  const [email, setEmail] = useState('');
  const [selected, setSelected] = useState('');

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user.user);

  const checkIdDuplicated = (e) => {
    setId(e.target.value);
    setIsUsableId(false);
    // 아이디 바뀔 때마다 가져옴
    axios
      .post('url', {
        id: id,
      })
      .then((response) => {
        // response
        if (response.isUsable) {
          setIsUsableId(true);
        }
      })
      .catch((error) => {
        // 오류발생시 실행
      })
      .then(() => {
        // 항상 실행
      });
  };

  const handleSignUp = () => {
    axios
      .post('url', {
        id: id,
        password: password,
      })
      .then((response) => {
        // response
        if (response.isUsable) {
          setIsUsableId(true);
        }
      })
      .catch((error) => {
        // 오류발생시 실행
      })
      .then(() => {
        // 항상 실행
      });
    navigate('/');
  };

  return (
    <SignUpWrapper>
      <Logo />
      <SignUpForm id="login-form" onSubmit={handleSignUp}>
        <SignUpTitle>회원가입</SignUpTitle>

        <LabelContainer>
          <Input
            id="id"
            type="text"
            className="has-label"
            placeholder="아이디"
            onChange={(e) => checkIdDuplicated(e)}
          />
          {isUsableId ? (
            <AddedMessage isCorrect={true}>사용할 수 있는 아이디입니다.</AddedMessage>
          ) : (
            <AddedMessage isCorrect={false}>사용할 수 없는 아이디입니다.</AddedMessage>
          )}
        </LabelContainer>

        <Input
          id="password"
          type="password"
          className="Password"
          placeholder="패스워드"
          onChange={(e) => setPassword(e.target.value)}
        />
        <LabelContainer>
          <Input
            name
            id="password-checking"
            type="password"
            className="has-label"
            placeholder="패스워드 확인"
            onChange={(e) => setPasswordChecking(e.target.value)}
          />
          {password.length >= 8 ? (
            password === passwordChecking ? (
              <AddedMessage isCorrect={true}>비밀번호가 일치합니다.</AddedMessage>
            ) : (
              <AddedMessage isCorrect={false}>비밀번호가 일치하지 않습니다.</AddedMessage>
            )
          ) : (
            <AddedMessage isCorrect={false}>비밀번호를 8자리 이상으로 설정해주세요.</AddedMessage>
          )}
        </LabelContainer>
        <Input type="text" className="Email" placeholder="이메일" />
        <SelectInput name="position" id="position">
          <option value="" selected disabled hidden>
            선택해주세요
          </option>
          <option value="pos">포스기</option>
          <option value="advisor">상담원</option>
        </SelectInput>
        <StyledButton
          type="submit"
          //   disabled={
          //     () => {
          //     return (
          //       isUsableId &&
          //       password.length >= 8 &&
          //       password === passwordChecking &&
          //       email &&
          //       selected
          //     );
          //   }
          // }
        >
          회원가입
        </StyledButton>
      </SignUpForm>
    </SignUpWrapper>
  );
}

export default SignUp;

const JobList = ['상담원', '점장'];
