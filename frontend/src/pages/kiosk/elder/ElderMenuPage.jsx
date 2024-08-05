import React from "react";
import { Router, Route, Routes, useNavigate } from "react-router-dom";
import Menu from "../../../components/kiosk/Menu";

function ElderMenuPage() {
  return (
    <div className="ElderMenuPage">
      this is elder menu-page
      {/* 비디오 화면 추가 */}
      <Menu />
    </div>
  );
}

export default ElderMenuPage;
