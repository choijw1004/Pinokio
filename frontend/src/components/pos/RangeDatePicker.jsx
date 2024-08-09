import React, { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker';
import styled from 'styled-components';

const DatePickerWrapper = styled.div`
  width: 29%;
  .react-datepicker-wrapper {
    width: 100%;
    font-family: 'CafeOhsquareAir';
    input {
      width: 100%;
      height: 3rem;
      display: flex;
      text-align: center;
      line-height: 3rem;
    }
  }
  .react-datepicker {
    font-family: 'CafeOhsquareAir';
  }
  .react-datepicker__day--range-end,
  .react-datepicker__day--in-range,
  .react-datepicker__day--in-selecting-range {
    background-color: rgb(115, 146, 255, 1);
  }
  .react-datepicker__day--selecting-range-start,
  .react-datepicker__day--keyboard-selected {
    background-color: rgb(115, 146, 255, 0.5);
  }
`;

function RangeDatePicker({ setDateRange }) {
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [dateRangeTemp, setDateRangeTemp] = useState([new Date(), new Date()]);
  const [startDateTemp, endDateTemp] = dateRangeTemp;

  useEffect(() => {
    if (endDateTemp !== null) {
      setDateRange(dateRangeTemp);
    }
  }, [dateRangeTemp]);

  return (
    <DatePickerWrapper>
      <DatePicker
        selectsRange={true}
        startDate={startDateTemp}
        endDate={endDateTemp}
        maxDate={new Date()}
        onChange={(update) => {
          console.log('s', startDateTemp);
          console.log('e ', endDateTemp);

          setDateRangeTemp(update);
        }}
        // wrapperClassName=
        // customInput={
        //   <input type="text" placeholder={'날짜를 입력해주세요'} style={{ width: '100%' }} />
        // }
      />
    </DatePickerWrapper>
  );
}
export default RangeDatePicker;
