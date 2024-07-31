import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import DescriptionCard from '../../components/pos/DescriptionCard';
import { Bar } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { ko } from 'date-fns/locale';

Chart.register(...registerables);

const MainOuter = styled.div`
  display: flex;
  flex-direction: row;
`;

const Main = styled.div`
  display: flex;
  flex-direction: column;
`;

const MainHeader = styled.div`
  display: flex;
  flex-direction: row;
`;

const MainBody = styled.div`
  display: flex;
  flex-direction: column;
`;

const Descriptions = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  border: solid 1px black;
`;

const Charts = styled.div`
  border: 1px;
`;

const DateDisplay = styled.div`
  border: 1px solid;
  cursor: pointer;
  width: 475px;
  padding: 10px;
  margin-bottom: 5px;
  text-align: center;
  user-select: none;
  border-radius: 15px;
`;

const DatePickerWrapper = styled.div`
  display: flex;
  gap: 10px;

  /* 전체 레이아웃. 그림자 효과같은 것을 주면 좋다 */
  .react-datepicker {
    box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25);
  }

  .react-datepicker__month-container {
  }

  .react-datepicker__header {
    background-color: white;
  }

  /* 최상단에 뜨는 연도와 월 */
  .react-datepicker__current-month {
  }

  /* 요일들 */
  .react-datepicker__day-name {
  }

  /* 날짜들을 담는 레이아웃 */
  .react-datepicker__month {
  }

  /* day: 주말 날짜 */
  .react-datepicker__day:nth-child(1) {
    color: red; /* 일요일 날짜*/
  }
  .react-datepicker__day:nth-child(7) {
    color: #8685ff; /* 토요일 날짜 */
  }

  /* day-name: 요일 */
  .react-datepicker__day-name:nth-child(1) {
    color: #ff5555; /* 일요일 */
  }
  .react-datepicker__day-name:nth-child(7) {
    color: #8685ff; /* 토요일 */
  }

  /* 일반 날짜 */
  .react-datepicker__day {
  }

  /* 선택된 날짜 */
  .react-datepicker__day--selected {
  }

  /* highlighted된 날짜 */
  .react-datepicker__day--highlighted {
  }

  /* 날짜에 마우스를 올릴 때 */
  .react-datepicker__day:hover {
  }
`;

function SalesReportPage() {
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const [isDatePickerOpen, setIsDatePickerOpen] = useState(false);

  useEffect(() => {
    setStartDate(new Date());
    setEndDate(new Date());
  }, []);

  const handleTodayClick = () => {
    const today = new Date();
    setStartDate(today);
    setEndDate(today);
  };

  const handleYesterdayClick = () => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    setStartDate(yesterday);
    setEndDate(yesterday);
  };

  const handleThisWeekClick = () => {
    const today = new Date();
    const firstDayOfWeek = new Date(today);
    firstDayOfWeek.setDate(today.getDate() - today.getDay() + 1);
    const lastDayOfWeek = new Date(today);
    lastDayOfWeek.setDate(firstDayOfWeek.getDate() + 6);
    setStartDate(firstDayOfWeek);
    setEndDate(lastDayOfWeek);
  };

  const handleThisMonthClick = () => {
    const today = new Date();
    const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
    const lastDayOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    setStartDate(firstDayOfMonth);
    setEndDate(lastDayOfMonth);
  };

  const formatDate = (date) => {
    return date instanceof Date && !isNaN(date.getTime()) ? date.toLocaleDateString() : '';
  };

  const data = {
    labels: ['1월', '2월', '3월', '4월', '5월', '6월', '7월'],
    datasets: [
      {
        label: 'My First dataset',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1,
        hoverBackgroundColor: 'rgba(255, 99, 132, 0.4)',
        hoverBorderColor: 'rgba(255, 99, 132, 1)',
        data: [65, 59, 80, 81, 56, 55, 40],
      },
    ],
  };

  const options = {
    scales: {
      x: {
        type: 'category',
        labels: ['1월', '2월', '3월', '4월', '5월', '6월', '7월'],
      },
    },
  };

  return (
    <MainOuter>
      <Main>
        <div>매출 리포트</div>
        <MainHeader>
          <div>
            <button onClick={handleYesterdayClick}>어제</button>
            <button onClick={handleTodayClick}>오늘</button>
            <button onClick={handleThisWeekClick}>이번주</button>
            <button onClick={handleThisMonthClick}>이번달</button>
          </div>
          <div>
            <DateDisplay onClick={() => setIsDatePickerOpen(!isDatePickerOpen)} tabIndex="0">
              {formatDate(startDate)} ~ {formatDate(endDate)}
            </DateDisplay>
            {isDatePickerOpen && (
              <DatePickerWrapper>
                <DatePicker
                  selected={startDate}
                  onChange={(date) => setStartDate(date)}
                  selectsStart
                  startDate={startDate}
                  endDate={endDate}
                  inline
                  locale={ko}
                />
                <DatePicker
                  selected={endDate}
                  onChange={(date) => setEndDate(date)}
                  selectsEnd
                  startDate={startDate}
                  endDate={endDate}
                  inline
                  locale={ko}
                />
              </DatePickerWrapper>
            )}
          </div>
        </MainHeader>
        <MainBody>
          <Descriptions>
            <DescriptionCard
              title={'실매출'}
              contents={'135000'}
              notice={'지난 주 수요일보다 +15,000원  늘었어요.'}
            ></DescriptionCard>
            <DescriptionCard
              title={'주문건'}
              contents={'5건'}
              notice={'지난 주 수요일보다 1건 늘었어요.'}
            ></DescriptionCard>
            <DescriptionCard title={'할인'} contents={'0원'} notice={''}></DescriptionCard>
          </Descriptions>
          <Charts>
            <Bar data={data} options={options} />
          </Charts>
        </MainBody>
      </Main>
      <div>
        <button>^</button>
        <button>v</button>
      </div>
    </MainOuter>
  );
}

export default SalesReportPage;
