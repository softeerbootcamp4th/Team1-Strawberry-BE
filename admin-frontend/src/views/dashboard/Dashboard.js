import React, { useEffect, useState } from 'react';
import {
  CButton,
  CButtonGroup,
  CCard,
  CCardBody,
  CCol,
  CRow,
} from '@coreui/react';
import { BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Bar } from 'recharts';

const Dashboard = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
      // 실제 구매자 수를 가져오는 요청
      const purchaserResponse = await fetch('http://localhost:8080/api/v1/purchaser/analysis', {
        method: 'GET', // HTTP 메서드를 명시적으로 지정합니다.
        headers: {
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
            'Content-Type': 'application/json' // 필요에 따라 Content-Type도 추가할 수 있습니다.
        }
      });
      const purchaserResult = await purchaserResponse.json();
      
      // 총 참여자 수를 가져오는 요청
      const eventResponse = await fetch('http://localhost:8080/api/v1/event/analysis', {
          method: 'GET', // HTTP 메서드를 명시적으로 지정합니다.
          headers: {
              'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
              'Content-Type': 'application/json'
          }
      });
      const eventResult = await eventResponse.json();
        console.log("eventReuslt",eventResult);

        // 가져온 데이터를 recharts에서 사용할 수 있는 형식으로 변환
        const formattedData = eventResult.map((event) => {
          const purchaser = purchaserResult.find(p => p.eventId === event.eventId);
          return {
            name: `이벤트 ${event.eventId}`, // 이벤트 ID를 이름으로 사용
            '총 참여자': event.totalUsers, // 총 참여자 수
            '실제 구매자': purchaser ? purchaser.purchaserCount : 0, // 실제 구매자 수
          };
        });

        console.log(formattedData);
        setData(formattedData);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []); // 빈 배열을 의존성으로 사용하여 컴포넌트가 처음 렌더링될 때만 실행

  return (
    <>
      <CCard className="mb-4">
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="traffic" className="card-title mb-0">이벤트 참여자 추이</h4>
              <div className="small text-body-secondary">January - July 2023</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
              <CButtonGroup className="float-end me-3">
                {['Day', 'Month', 'Year'].map((value) => (
                  <CButton
                    color="outline-secondary"
                    key={value}
                    className="mx-0"
                    active={value === 'Month'}
                  >
                    {value}
                  </CButton>
                ))}
              </CButtonGroup>
            </CCol>
          </CRow>
          <BarChart width={730} height={500} data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis/>
            <Tooltip />
            <Legend />
            <Bar dataKey="총 참여자" fill="#8884d8" />
            <Bar dataKey="실제 구매자" fill="#82ca9d" />
          </BarChart>
        </CCardBody>
      </CCard>
    </>
  )
}

export default Dashboard;
