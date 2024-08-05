import React, { useEffect, useState } from "react";
import {
  Routes,
  Route,
  BrowserRouter as Router,
  useRoutes,
} from "react-router-dom";

import Login from "./pages/loginsignup/Login";
import SignUp from "./pages/loginsignup/SignUp";
import FindPassword1 from "./pages/loginsignup/FindPassword1";
import FindPassword2 from "./pages/loginsignup/FindPassword2";

import KioskIndex from "./pages/kiosk/KioskIndex";
import PosIndex from "./pages/pos/PosIndex";
import AdvIndex from "./pages/advisor/AdvIndex";

function App() {
  const [userId, setUserId] = useState("");
  useEffect(() => {
    setUserId(localStorage.getItem("id"));
    console.log(userId);
  });
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="signup" element={<SignUp />} />
          <Route path="findpassword1" element={<FindPassword1 />} />
          <Route path="findpassword2" element={<FindPassword2 />} />
          <Route path="/kiosk/*" element={<KioskIndex />} />
          <Route path="/pos/*" element={<PosIndex />} />
          <Route path="/advisor/*" element={<AdvIndex />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
