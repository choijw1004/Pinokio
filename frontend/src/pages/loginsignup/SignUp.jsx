import React from "react";
import "./Login.css";

function SignUp() {
  return (
    <div class="Login">
      <h2>회원가입</h2>
      <form action="server-url" id="signup-form">
        <input type="text" className="Id" placeholder="id"></input>
        <input type="text" className="Password" placeholder="pw"></input>
        <input type="text" className="Email" placeholder="email"></input>
        <button>회원가입</button>
      </form>
      <div></div>
    </div>
  );
}

export default SignUp;
