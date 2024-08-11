import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import styled from 'styled-components';

import LOGO from '../../components/common/Logo';
import { useDispatch } from 'react-redux';
import { setUser, clearUser } from '../../features/user/userSlice';
import {
  postLoginKiosk,
  postLoginPos,
  postLoginAdvisor,
  getKioskInfo,
  getPosInfo,
} from '../../apis/Auth';
import axios from '../../apis/Axios';
import Cookies from 'js-cookie';

const LoginWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f4f4f9;
  padding: 20px;
`;

const LoginForm = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  width: 100%;
  max-width: 400px;
  box-sizing: border-box;
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
    transform: translateY(-1px);
  }
  &:hover {
    background-color: #c383d9;
  }
  &:active {
    background-color: #7392ff;
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  flex-direction: row-reverse;
  margin-top: 20px;

  div {
    color: #7392ff;
    font-size: 12px;
    cursor: pointer;
    margin: 0 3px;

    &:hover {
      color: #d8ff75;
    }
    &:active {
      color: #7392ff;
    }
  }
`;

function Login() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [usertype, setUserType] = useState('');

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const findPassword = () => {
    navigate('/findpassword1');
  };

  const signUp = () => {
    navigate('/signup');
  };

  const handleUserType = (e) => {
    setUserType(e.target.value);
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('id : ', id);

    if (!id) {
      alert('아이디를 입력해주세요.');
      return;
    }

    if (!password) {
      alert('비밀번호를 입력해주세요.');
      return;
    }

    try {
      let res;

      dispatch(clearUser());
      // 로그인 API 호출
      if (usertype === 'pos') {
        res = await postLoginPos(id, password);
        console.log('POS login response:', res);
      } else if (usertype === 'advisor') {
        res = await postLoginAdvisor(id, password);
        console.log('advisor login response:', res);
      } else if (usertype === 'kiosk') {
        res = await postLoginKiosk(id, password);
        console.log('kiosk login response:', res);
      }

      let accessToken;
      let refreshToken;
      if (usertype === 'kiosk') {
        accessToken = res.authToken?.accessToken;
        refreshToken = res.authToken?.refreshToken;
      } else if (usertype === 'pos') {
        accessToken = res.accessToken;
        refreshToken = res.refreshToken;
      } else {
        accessToken = res.accessToken;
        refreshToken = res.refreshToken;
      }
      console.log(`accessToken : ${accessToken}`);

      if (accessToken) {
        console.log('yes');
        axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

        // accessToken을 localStorage에 저장
        localStorage.setItem('accessToken', accessToken);

        // refreshToken을 쿠키에 저장
        Cookies.set('refreshToken', refreshToken);

        // 사용자 데이터 준비
        const newUserData = {
          user: id,
          type: usertype,
          typeInfo:
            usertype === 'kiosk'
              ? await getKioskInfo()
              : usertype === 'pos'
              ? await getPosInfo()
              : null,
          token: accessToken,
        };

        console.log('userData before dispatch:', newUserData);
        console.log(`newUserData : ${newUserData.typeInfo}`);

        if (newUserData) {
          console.log(`userData : ${newUserData.user.id}`);
          dispatch(setUser(newUserData));
          console.log('Dispatch successful');
          navigate(`/${usertype}`);
        } else {
          console.error('사용자 데이터가 누락되었습니다.');
        }
      } else {
        console.error('토큰이 응답에 누락되었습니다.');
      }
    } catch (error) {
      console.error('로그인 실패:', error);
      alert('로그인에 실패했습니다.');
    }
  };

  return (
    <LoginWrapper>
      <LOGO />
      <LoginForm id="login-form" onSubmit={handleLogin}>
        <Input
          type="text"
          className="id"
          placeholder="아이디"
          onChange={(e) => setId(e.target.value)}
        />
        <Input
          type="password"
          className="password"
          placeholder="패스워드"
          onChange={(e) => setPassword(e.target.value)}
        />
        <select value={usertype} onChange={handleUserType}>
          <option value="" disabled>
            선택하세요
          </option>
          <option value="kiosk">키오스크</option>
          <option value="pos">포스</option>
          <option value="advisor">상담원</option>
        </select>
        <StyledButton type="submit">로그인</StyledButton>
      </LoginForm>
      <ButtonWrapper>
        <div onClick={findPassword}>비밀번호 찾기</div>
        <div>|</div>
        <div onClick={signUp}>회원가입</div>
      </ButtonWrapper>
    </LoginWrapper>
  );
}

export default Login;
