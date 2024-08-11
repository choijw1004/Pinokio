import React, { useState } from 'react';

const ToggleIcon = ({ isOn, setIsOn }) => {
  // const [isOn, setIsOn] = useState(false);

  const toggleIcon = () => {
    setIsOn(!isOn);
  };

  return (
    <div
      onClick={toggleIcon}
      style={{
        cursor: 'pointer',
        width: '64px',
        height: '64px',
        display: 'flex',
        justifyContent: 'center',
      }}
    >
      {isOn ? (
        <img src="/NavBarOn.svg" width="45rem" />
      ) : (
        <img src="/NavBarOff.svg" width="45rem" />
      )}
    </div>
  );
};

export default ToggleIcon;
