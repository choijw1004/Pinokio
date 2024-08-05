import React from "react";

function Button({ onClick, text, type }) {
  return (
    <button className="Button" onClick={onClick} type={type}>
      {text}
    </button>
  );
}

export default Button;
