import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CRow,
    CTable,
    CTableBody,
    CTableCaption,
    CTableDataCell,
    CTableHead,
    CTableHeaderCell,
    CTableRow,
    CButton,
  } from '@coreui/react'
  
const EventDetail = () => {
    const { subEventId } = useParams(); // URL에서 이벤트 ID를 가져옴
    const [events, setEvents] = useState(null); // 단일 이벤트 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
    
    // API 호출
    const fetchEventDetails = async (id = subEventId, page = 0, size = 10) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/eventuser/list?subEventId=${id}&page=${page}&size=${size}`);
            if (!response.ok) {
                throw new Error('네트워크 응답이 올바르지 않습니다');
            }
            const data = await response.json();
            setEvents(data.content); // 이벤트 리스트 업데이트
            setTotalPages(data.totalPages); // 총 페이지 수 업데이트
            console.log(data);
        } catch (error) {
            setError(error.message); // 에러 업데이트
        } finally {
            setLoading(false); // 로딩 완료
        }
    };

    const handleViewParticipants = (subEventId) => {
        window.location.href = `/#/event/` + eventId + `/` + subEventId; // 페이지 이동
    };

    // 컴포넌트가 마운트될 때 이벤트를 가져옴
    useEffect(() => {
        fetchEventDetails();
    }, [subEventId]); // eventId가 변경될 때마다 호출

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div>에러 발생: {error}</div>;
    }

    return (
        <CRow>
            <CCol xs={12}>
                <CCard className="mb-4">
                    <CCardHeader>
                        <strong>이벤트 참여자 리스트</strong>
                    </CCardHeader>
                    <CCardBody>
                        <CTable>
                            <CTableHead>
                                <CTableRow>
                                    <CTableHeaderCell>Id</CTableHeaderCell>
                                    <CTableHeaderCell>이름</CTableHeaderCell>
                                    <CTableHeaderCell>이메일</CTableHeaderCell>
                                    <CTableHeaderCell>전화번호</CTableHeaderCell>
                                    <CTableHeaderCell>당첨 여부</CTableHeaderCell>
                                </CTableRow>
                            </CTableHead>
                            <CTableBody>
                                {events.map((event) => (
                                    <CTableRow key={event.userId}>
                                        <CTableDataCell>{event.userId}</CTableDataCell>
                                        <CTableDataCell>{event.name}</CTableDataCell>
                                        <CTableDataCell>{event.email}</CTableDataCell>
                                        <CTableDataCell>{event.phoneNumber}</CTableDataCell>
                                        <CTableDataCell>{event.winner? "당첨" : ""}</CTableDataCell>
                                    </CTableRow>
                                ))}
                            </CTableBody>
                        </CTable>
                        <div className="mt-3">
                            {Array.from({ length: totalPages }, (_, index) => (
                                <button
                                    key={index}
                                    onClick={() => handlePageChange(index)}
                                    disabled={currentPage === index} // 현재 페이지는 비활성화
                                    className="btn btn-primary mx-1"
                                >
                                    {index + 1}
                                </button>
                            ))}
                        </div>
                    </CCardBody>
                </CCard>
            </CCol>
        </CRow>
    );
};

export default EventDetail;
