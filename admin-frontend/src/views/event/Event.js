import React, { useEffect, useState } from 'react';
import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CRow,
    CTable,
    CTableBody,
    CTableDataCell,
    CTableHead,
    CTableHeaderCell,
    CTableRow,
    CButton,
    CModal,
    CModalHeader,
    CModalTitle,
    CModalBody,
    CModalFooter
} from '@coreui/react';

const EventList = () => {
    const [events, setEvents] = useState([]); // 이벤트 리스트 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
    const [visible, setVisible] = useState(false); // 모달 상태
    const [selectedEvent, setSelectedEvent] = useState(null); // 선택된 이벤트 상태

    // 이벤트 수정 api
    const handleSaveChanges = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/event/${selectedEvent.id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    // ADMIN 에 대한 로그인을 따로 구현하지 않았기에 임시적으로 토큰을 넣어두었습니다.
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(selectedEvent),
            });

            if (!response.ok) {
                throw new Error('Failed to update event');
            }

            // Update the events list after successful update
            fetchEvents(currentPage);
            setVisible(false); // Close the modal
        } catch (error) {
            setError(error.message); // Display the error message
        }
    };

    // API 호출
    const fetchEvents = async (page = 0) => {
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/event/list?page=${page}`, {
                method: 'GET', // HTTP 메서드를 명시적으로 지정합니다.
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    // ADMIN 에 대한 로그인을 따로 구현하지 않았기에 임시적으로 토큰을 넣어두었습니다.
                    'Content-Type': 'application/json' // 필요에 따라 Content-Type도 추가할 수 있습니다.
                }
            });
            
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

    // 컴포넌트가 마운트될 때 이벤트를 가져옴
    useEffect(() => {
        fetchEvents(currentPage);
    }, [currentPage]); // currentPage가 변경될 때마다 호출

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const handleViewDetails = (eventId) => {
        window.location.href = `/#/event/${eventId}`; // 페이지 이동
    };

    const handleModify = (event) => {
        setSelectedEvent(event);  // Set the selected event details
        setVisible(true);         // Show the modal
    }

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
                        <strong>이벤트 리스트</strong>
                    </CCardHeader>
                    <CCardBody>
                        <CTable>
                            <CTableHead>
                                <CTableRow>
                                    <CTableHeaderCell>이벤트 이름</CTableHeaderCell>
                                    <CTableHeaderCell>시작일</CTableHeaderCell>
                                    <CTableHeaderCell>종료일</CTableHeaderCell>
                                    <CTableHeaderCell>차량명</CTableHeaderCell>
                                        <CTableHeaderCell>수정</CTableHeaderCell>
                                        <CTableHeaderCell>조회</CTableHeaderCell>
                                </CTableRow>
                            </CTableHead>
                            <CTableBody>
                                {events.map((event) => (
                                    <CTableRow key={event.id}>
                                        <CTableDataCell>{event.eventName}</CTableDataCell>
                                        <CTableDataCell>{new Date(event.startAt).toLocaleString()}</CTableDataCell>
                                        <CTableDataCell>{new Date(event.endAt).toLocaleString()}</CTableDataCell>
                                        <CTableDataCell>{event.carName}</CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="light"
                                                onClick={() => handleModify(event)}
                                            >
                                                수정
                                            </CButton>
                                        </CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="light"
                                                onClick={() => handleViewDetails(event.id)}
                                            >
                                                조회
                                            </CButton>
                                        </CTableDataCell>
                                    </CTableRow>
                                ))}
                            </CTableBody>
                        </CTable>

                        {/* Modal for modifying event */}
                        <CModal visible={visible} onClose={() => setVisible(false)} aria-labelledby="LiveDemoExampleLabel">
                            <CModalHeader>
                                <CModalTitle id="LiveDemoExampleLabel">이벤트 수정</CModalTitle>
                            </CModalHeader>
                            <CModalBody>
                                {selectedEvent && (
                                    <CTable>
                                        <CTableHead>
                                            <CTableRow>
                                                <CTableHeaderCell>시작일</CTableHeaderCell>
                                                <CTableHeaderCell>종료일</CTableHeaderCell>
                                            </CTableRow>
                                        </CTableHead>
                                        <CTableBody>
                                            <CTableRow>
                                                <CTableDataCell>
                                                    <input
                                                        type="datetime-local"
                                                        value={new Date(selectedEvent.startAt).toISOString().slice(0, 16)}
                                                        onChange={(e) => setSelectedEvent({ ...selectedEvent, startAt: new Date(e.target.value).toISOString() })}
                                                    />
                                                </CTableDataCell>
                                                <CTableDataCell>
                                                    <input
                                                        type="datetime-local"
                                                        value={new Date(selectedEvent.endAt).toISOString().slice(0, 16)}
                                                        onChange={(e) => setSelectedEvent({ ...selectedEvent, endAt: new Date(e.target.value).toISOString() })}
                                                    />
                                                </CTableDataCell>
                                            </CTableRow>
                                        </CTableBody>
                                    </CTable>
                                )}
                            </CModalBody>
                            <CModalFooter>
                                <CButton color="primary" onClick={handleSaveChanges}>Save Changes</CButton>
                                <CButton color="secondary" onClick={() => setVisible(false)}>Close</CButton>
                            </CModalFooter>
                        </CModal>

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

export default EventList;
