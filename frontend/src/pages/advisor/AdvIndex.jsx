import React, { useState } from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import AdvNowPage from "./AdvNowPage";
import AdvLoadingPage from "./AdvLoadingPage";

function AdvIndex() {
  return (
    <div className="Adv-index">
      <Routes>
        <Route path="/" element={<AdvLoadingPage />} />
        <Route path="advisor-now" element={<AdvNowPage />} />
      </Routes>
    </div>
  );
}

export default AdvIndex;
