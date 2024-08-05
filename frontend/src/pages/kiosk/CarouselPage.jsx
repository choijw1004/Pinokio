import React from "react";
import Button from "../../components/common/Button";
import { Router, Route, Routes, useNavigate } from "react-router-dom";

function CarouselPage() {
  const navigate = useNavigate();

  const havingHere = () => {
    navigate("/kiosk/menu", { state: { where: "having here" } });
  };

  const takeAway = () => {
    navigate("/kiosk/menu", { state: { where: "take away" } });
  };

  return (
    <div className="CarouselPage">
      this is carousel-page
      <Button onClick={havingHere} text="매장" />
      <Button onClick={takeAway} text="포장" />
    </div>
  );
}

export default CarouselPage;
