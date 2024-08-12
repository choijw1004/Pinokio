import React, { useState } from 'react';

const ToggleIcon = ({ isOn, setIsOn }) => {
  // const [isOn, setIsOn] = useState(false);

  const toggleIcon = () => {
    setIsOn(!isOn);
  };

  return (
    <div onClick={toggleIcon} style={{ cursor: 'pointer', width: '64px', height: '64px' }}>
      {isOn ? (
        <svg
          width="64"
          height="64"
          viewBox="0 0 64 64"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <rect
            x="11.5"
            y="11.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#7392FF"
            stroke-width="3"
          />
          <rect
            x="31.9787"
            y="20"
            width="17"
            height="17"
            rx="1.5"
            transform="rotate(-45 31.9787 20)"
            stroke="#EC7348"
            stroke-width="3"
          />
          <rect
            x="11.5"
            y="35.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#C383D9"
            stroke-width="3"
          />
          <rect
            x="35.5"
            y="35.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#FFC33F"
            stroke-width="3"
          />
        </svg>
      ) : (
        <svg
          width="64"
          height="64"
          viewBox="0 0 64 64"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <rect
            x="11.5"
            y="11.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#7392FF"
            stroke-width="3"
          />
          <rect
            x="35.5"
            y="11.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#EC7348"
            stroke-width="3"
          />
          <rect
            x="11.5"
            y="35.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#C383D9"
            stroke-width="3"
          />
          <rect
            x="35.5"
            y="35.5"
            width="17"
            height="17"
            rx="1.5"
            stroke="#FFC33F"
            stroke-width="3"
          />
        </svg>
      )}
    </div>
  );
};

export default ToggleIcon;
