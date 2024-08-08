import React, { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker';

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
      isClearable={true}
    />
  );
}
export default RangeDatePicker;
