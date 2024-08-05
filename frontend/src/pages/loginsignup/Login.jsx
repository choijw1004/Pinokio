import React from "react";
import "./Login.css";
import Button from "../../components/common/Button";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function Login() {
  const [id, setId] = useState("");
  const navigate = useNavigate();

  const findPassword = () => {
    navigate("/findpassword1");
  };
  const signUp = () => {
    navigate("/signup");
  };

  const handleLogin = (e) => {
    e.preventDefault(); // 기본 폼 제출 방지
    if (id === "advisor") {
      navigate("/advisor");
    } else if (id === "pos") {
      navigate("/pos");
    } else {
      navigate("/kiosk");
    }
  };

  return (
    <div className="Login">
      <h2>로그인</h2>
      <form id="login-form" onSubmit={handleLogin}>
        <input
          type="text"
          className="Id"
          placeholder="id"
          onChange={(e) => setId(e.target.value)}
        ></input>
        <input type="text" className="Password" placeholder="pw"></input>
        <Button type="submit" text="로그인" />
      </form>
      <div>
        <Button onClick={findPassword} text="비밀번호 찾기" />
        <Button onClick={signUp} text="회원가입">
          회원가입
        </Button>
      </div>
    </div>
  );
}

export default Login;
