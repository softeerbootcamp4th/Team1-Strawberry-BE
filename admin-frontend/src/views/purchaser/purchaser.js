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
} from '@coreui/react';

const Purchasers = () => {
    const [events, setEvents] = useState([]); // 이벤트 리스트 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
    const [pagesToShow, setPagesToShow] = useState([]); // 표시할 페이지들

    // API 호출
    const fetchEvents = async (page = 0) => {
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/purchaser/list?page=${page}`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('네트워크 응답이 올바르지 않습니다');
            }
            const data = await response.json();
            setEvents(data.content); // 이벤트 리스트 업데이트
            setTotalPages(data.totalPages); // 총 페이지 수 업데이트
            updatePagesToShow(0, data.totalPages); // 초기 페이지 업데이트
        } catch (error) {
            setError(error.message); // 에러 업데이트
        } finally {
            setLoading(false); // 로딩 완료
        }
    };

    // 표시할 페이지 번호를 계산
    const updatePagesToShow = (currentPage, totalPages) => {
        const pages = [];
        const startPage = Math.floor(currentPage / 10) * 10;
        for (let i = startPage; i < Math.min(startPage + 10, totalPages); i++) {
            pages.push(i);
        }
        setPagesToShow(pages);
    };

    // 컴포넌트가 마운트될 때 이벤트를 가져옴
    useEffect(() => {
        fetchEvents(currentPage);
    }, [currentPage]);

    const handlePageChange = (page) => {
        setCurrentPage(page);
        updatePagesToShow(page, totalPages);
    };

    const handleViewDetails = (eventId) => {
        window.location.href = `/#/event/${eventId}`;
    };

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
                                    <CTableHeaderCell>Id</CTableHeaderCell>
                                    <CTableHeaderCell>유저 아이디</CTableHeaderCell>
                                    <CTableHeaderCell>차 이름</CTableHeaderCell>
                                </CTableRow>
                            </CTableHead>
                            <CTableBody>
                                {events.map((event) => (
                                    <CTableRow key={event.id}>
                                        <CTableDataCell>{event.id}</CTableDataCell>
                                        <CTableDataCell>{event.userId}</CTableDataCell>
                                        <CTableDataCell>{event.carName}</CTableDataCell>
                                    </CTableRow>
                                ))}
                            </CTableBody>
                        </CTable>
                        <div className="mt-3">
                            {currentPage > 0 && (
                                <button
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    className="btn btn-secondary mx-1"
                                >
                                    이전
                                </button>
                            )}
                            {pagesToShow.map((page) => (
                                <button
                                    key={page}
                                    onClick={() => handlePageChange(page)}
                                    disabled={currentPage === page}
                                    className="btn btn-primary mx-1"
                                >
                                    {page + 1}
                                </button>
                            ))}
                            {currentPage < totalPages - 1 && (
                                <button
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    className="btn btn-secondary mx-1"
                                >
                                    다음
                                </button>
                            )}
                        </div>
                    </CCardBody>
                </CCard>
            </CCol>
        </CRow>
    );
};

export default Purchasers;
