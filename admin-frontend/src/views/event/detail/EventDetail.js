
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
    CTableHead,
    CTableHeaderCell,
    CTableRow,
    CButton,
    CModal,
    CModalHeader,
    CModalTitle,
    CModalBody,
    CModalFooter,
    CTableDataCell,
} from '@coreui/react';

const EventDetail = () => {
    const { eventId } = useParams(); // URL에서 이벤트 ID를 가져옴
    const [events, setEvents] = useState(null); // 단일 이벤트 상태
    const [winners, setWinners] = useState(null); // 당첨자 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
    const [visible, setVisible] = useState(false); // 당첨자 조회 모달 상태
    const [editModalVisible, setEditModalVisible] = useState(false); // 수정 모달 상태
    const [editModalEventVisible, setEditModalEventVisible] = useState(false); // 수정 모달 상태
    const [selectedEvent, setSelectedEvent] = useState(null); // 선택한 이벤트 상태
    const [editablePrizeInfo, setEditablePrizeInfo] = useState([]); // Editable prize info state

    const prizeInfo = [
        { rank: 1, amount: 100, prizeId: '3' },
        { rank: 2, amount: 500, prizeId: '1' },
        { rank: 3, amount: 500, prizeId: '2' },
    ]

    // 당첨 설정 저장 API 호출
    const handleSavePrizeSettings = async () => {
        try {
            // PUT 요청을 보낼 데이터 준비
            const response = await fetch(`http://localhost:8080/api/v1/subevent/${selectedEvent.id}/winner`, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    // ADMIN 에 대한 로그인을 따로 구현하지 않았기에 임시적으로 토큰을 넣어두었습니다.
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(editablePrizeInfo), // editablePrizeInfo 배열을 JSON 형식으로 변환하여 전송
            });

            if (!response.ok) {
                throw new Error('Failed to update prize settings');
            }

            // 요청이 성공적으로 완료되면 모달을 닫습니다.
            setEditModalVisible(false);
        } catch (error) {
            setError(error.message); // 에러가 발생하면 에러 메시지를 상태로 설정
            console.error('Error updating prize settings:', error);
        }
    };


    // 이벤트 상세정보 API 호출
    const fetchEventDetails = async (id = eventId, page = 0, size = 10) => {
        setLoading(true); // 로딩 상태 설정
        try {
            const response = await fetch(`http://localhost:8080/api/v1/subevent/list?eventId=${id}&page=${page}&size=${size}`, {
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
            setError(error.message); // 에러 상태 업데이트
        } finally {
            setLoading(false); // 로딩 상태 해제
        }
    };

    // 당첨자 정보 API 호출
    const fetchWinners = async (subEventId) => {
        setLoading(true); // 로딩 상태 설정
        try {
            const response = await fetch(`http://localhost:8080/api/v1/subevent/winner?subEventId=${subEventId}`, {
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
            setWinners(data.data); // 당첨자 리스트 업데이트
            console.log(data);
        } catch (error) {
            setError(error.message); // 에러 상태 업데이트
        } finally {
            setLoading(false); // 로딩 상태 해제
        }
    };

    // 이벤트 날짜 수정 API 호출
    const handleSaveChanges = async () => {
        try {
            // startAt과 endAt만 포함하는 새로운 객체를 생성
            const updatedEvent = {
                startAt: selectedEvent.startAt,
                endAt: selectedEvent.endAt
            };

            const response = await fetch(`http://localhost:8080/api/v1/subevent/${selectedEvent.id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    // ADMIN 에 대한 로그인을 따로 구현하지 않았기에 임시적으로 토큰을 넣어두었습니다.
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedEvent), // startAt과 endAt만 전송
            });

            if (!response.ok) {
                throw new Error('Failed to update event');
            }

            // Refresh the events list after a successful update
            fetchEventDetails(eventId, currentPage);
            setEditModalEventVisible(false); // Close the edit modal
        } catch (error) {
            setError(error.message); // Display the error message
        }
    };

    const handleViewParticipants = (subEventId) => {
        window.location.href = `/#/event/` + eventId + `/` + subEventId; // 페이지 이동
    };

    // 당첨자 추첨 버튼 핸들러
    const handleDrawWinners = async (subEventId) => {
        try {
            // subEventId를 쿼리 파라미터로 설정
            const queryParams = new URLSearchParams({ subEventId });
    
            // GET 요청을 사용하여 추첨 엔드포인트 호출
            const response = await fetch(`http://localhost:8080/api/v1/subevent/draw?${queryParams.toString()}`, {
                method: 'GET', // GET 요청으로 변경
                headers: {
                    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImV4cCI6MTcyNTAxOTIwM30.Y9N-pmmrNrEIDVDuoY0sdvQRVQj1TIJq2TokuSZK2L7yQPKctq4kZBc9OyikMGBPPXD0Ig8u6TOZ-JNVyRHpGg',
                    // ADMIN 에 대한 로그인을 따로 구현하지 않았기에 임시적으로 토큰을 넣어두었습니다.
                    'Content-Type': 'application/json' // 필요에 따라 Content-Type도 추가할 수 있습니다.
                }
            });
    
            if (!response.ok) {
                throw new Error('Failed to draw winners');
            }
    
            // 성공적으로 추첨한 후, 당첨자 목록을 가져옵니다.
            fetchWinners(subEventId);
        } catch (error) {
            setError(error.message); // 에러 메시지를 상태로 설정
        }
    }

    // 당첨자 수정 버튼 핸들러
    const handleModify = (event) => {
        setSelectedEvent(event); // 수정할 이벤트 선택

        // winnersMeta 정보를 사용하여 editablePrizeInfo 설정
        const eventPrizeInfo = Object.entries(event.winnersMeta).map(([rank, winnerMeta]) => ({
            rank: parseInt(rank, 10), // 문자열로 된 키를 숫자로 변환하여 rank로 사용
            amount: winnerMeta.winnerCount,
            prizeId: winnerMeta.prizeId
        }));

        setEditablePrizeInfo(eventPrizeInfo); // editablePrizeInfo 상태 설정
        setEditModalVisible(true); // 수정 모달 표시
    };


    // 이벤트 수정 버튼 핸들러
    const handleEventModify = (event) => {
        setSelectedEvent(event); // 수정할 이벤트 선택
        setEditModalEventVisible(true); // 수정 모달 표시
    };

    // 페이지 로드 시 이벤트 상세 정보 가져오기
    useEffect(() => {
        fetchEventDetails();
    }, [eventId]); // eventId가 변경될 때마다 호출

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
                        <strong>이벤트 상세</strong>
                    </CCardHeader>
                    <CCardBody>
                        <CTable>
                            <CTableHead>
                                <CTableRow>
                                    <CTableHeaderCell>이벤트 이름</CTableHeaderCell>
                                    <CTableHeaderCell>시작일</CTableHeaderCell>
                                    <CTableHeaderCell>종료일</CTableHeaderCell>
                                    <CTableHeaderCell>이벤트 종류</CTableHeaderCell>
                                    <CTableHeaderCell>추첨 종류</CTableHeaderCell>
                                    <CTableHeaderCell>당첨자 조회</CTableHeaderCell>
                                    <CTableHeaderCell>당첨자 추첨</CTableHeaderCell>
                                    <CTableHeaderCell>당첨 설정</CTableHeaderCell>
                                    <CTableHeaderCell>이벤트 수정</CTableHeaderCell>
                                    <CTableHeaderCell>참여자 조회</CTableHeaderCell>
                                </CTableRow>
                            </CTableHead>
                            <CTableBody>
                                {events.map((event) => (
                                    <CTableRow key={event.id}>
                                        <CTableDataCell>{event.alias}</CTableDataCell>
                                        <CTableDataCell>{new Date(event.startAt).toLocaleString()}</CTableDataCell>
                                        <CTableDataCell>{new Date(event.endAt).toLocaleString()}</CTableDataCell>
                                        <CTableDataCell>{event.subEventExecuteType}</CTableDataCell>
                                        <CTableDataCell>{event.subEventType}</CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="primary"
                                                onClick={() => {
                                                    setVisible(true); // Show the winners modal
                                                    fetchWinners(event.id); // Fetch winners for the selected sub-event
                                                }}
                                            >
                                                조회
                                            </CButton>
                                        </CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="success"
                                                onClick={() => handleDrawWinners(event.id)}
                                                disabled={event.subEventExecuteType === 'FIRSTCOME'}
                                            >
                                                추첨
                                            </CButton>
                                        </CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="warning"
                                                onClick={() => handleModify(event)}
                                            >
                                                설정
                                            </CButton>
                                        </CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="warning"
                                                onClick={() => handleEventModify(event)}
                                            >
                                                수정
                                            </CButton>
                                        </CTableDataCell>
                                        <CTableDataCell>
                                            <CButton
                                                color="light"
                                                onClick={() => handleViewParticipants(event.id)}
                                            >
                                                조회
                                            </CButton>
                                        </CTableDataCell>
                                    </CTableRow>
                                ))}
                            </CTableBody>
                        </CTable>

                        {/* Pagination */}
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

            {/* 이벤트 수정 모달 */}
            <CModal
                visible={editModalEventVisible}  // 여기서 수정 모달의 가시성 상태를 사용합니다.
                onClose={() => setEditModalEventVisible(false)}  // onClose 시 이벤트 수정 모달을 닫습니다.
                aria-labelledby="EditEventModalLabel"
            >
                <CModalHeader>
                    <CModalTitle id="EditEventModalLabel">이벤트 수정</CModalTitle>
                </CModalHeader>
                <CModalBody>
                    {selectedEvent && (
                        <CTable>
                            <CTableHead>
                                <CTableRow>
                                    <CTableHeaderCell>시작 날짜</CTableHeaderCell>
                                    <CTableHeaderCell>종료 날짜</CTableHeaderCell>
                                </CTableRow>
                            </CTableHead>
                            <CTableBody>
                                <CTableRow>
                                    <CTableDataCell>
                                        <input
                                            type="datetime-local"
                                            value={new Date(selectedEvent.startAt).toISOString().slice(0, -1)}
                                            onChange={(e) =>
                                                setSelectedEvent({ ...selectedEvent, startAt: new Date(e.target.value).toISOString() })
                                            }
                                        />
                                    </CTableDataCell>
                                    <CTableDataCell>
                                        <input
                                            type="datetime-local"
                                            value={new Date(selectedEvent.endAt).toISOString().slice(0, -1)}
                                            onChange={(e) =>
                                                setSelectedEvent({ ...selectedEvent, endAt: new Date(e.target.value).toISOString() })
                                            }
                                        />
                                    </CTableDataCell>
                                </CTableRow>
                            </CTableBody>
                        </CTable>
                    )}
                </CModalBody>
                <CModalFooter>
                    <CButton color="primary" onClick={handleSaveChanges}>
                        저장
                    </CButton>
                    <CButton color="secondary" onClick={() => setEditModalEventVisible(false)}>
                        닫기
                    </CButton>
                </CModalFooter>
            </CModal>


            {/* 당첨자 조회 모달 */}
            <CModal
                visible={visible}
                onClose={() => setVisible(false)}
                aria-labelledby="LiveDemoExampleLabel"
                className="modal-lg"
            >
                <CModalHeader>
                    <CModalTitle id="LiveDemoExampleLabel">Winners List</CModalTitle>
                </CModalHeader>
                <CModalBody>
                    <CTable>
                        <CTableHead>
                            <CTableRow>
                                <CTableHeaderCell>Rank</CTableHeaderCell>
                                <CTableHeaderCell>Name</CTableHeaderCell>
                                <CTableHeaderCell>Prize</CTableHeaderCell>
                            </CTableRow>
                        </CTableHead>
                        <CTableBody>
                        {winners && winners.map((winner, index) => (
                                <CTableRow keyß={index}>
                                    <CTableDataCell>{winner.ranking}</CTableDataCell>
                                    <CTableDataCell>{winner.userId}</CTableDataCell>
                                    <CTableDataCell>{winner.prizeName}</CTableDataCell>
                                </CTableRow>
                            ))}

                        </CTableBody>
                    </CTable>
                </CModalBody>
                <CModalFooter>
                    <CButton color="secondary" onClick={() => setVisible(false)}>
                        Close
                    </CButton>
                </CModalFooter>
            </CModal>

            {/* 당첨 설정 모달 */}
            <CModal
                visible={editModalVisible}
                onClose={() => setEditModalVisible(false)}
                aria-labelledby="PrizeSettingsModalLabel"
                className="modal-lg"
            >
                <CModalHeader>
                    <CModalTitle id="PrizeSettingsModalLabel">Prize Settings</CModalTitle>
                </CModalHeader>
                <CModalBody>
                    <CTable>
                        <CTableHead>
                            <CTableRow>
                                <CTableHeaderCell>Rank</CTableHeaderCell>
                                <CTableHeaderCell>PrizeId</CTableHeaderCell>
                                <CTableHeaderCell>Amount</CTableHeaderCell>
                            </CTableRow>
                        </CTableHead>
                        <CTableBody>
                            {editablePrizeInfo.map(({ rank, amount, prizeId }, index) => (
                                <CTableRow key={index}>
                                    <CTableDataCell>{rank}</CTableDataCell>
                                    <CTableDataCell>
                                        <input
                                            type="number"
                                            value={prizeId}
                                            onChange={(e) => {
                                                const updatedPrizeInfo = [...editablePrizeInfo]; // 상태 복사
                                                updatedPrizeInfo[index].prizeId = e.target.value; // 복사된 상태의 값을 업데이트
                                                setEditablePrizeInfo(updatedPrizeInfo); // 상태 업데이트
                                            }}
                                            style={{ width: '100%' }} // Fit the content
                                        />
                                    </CTableDataCell>
                                    <CTableDataCell>
                                        <input
                                            type="number"
                                            value={amount}
                                            onChange={(e) => {
                                                const updatedPrizeInfo = [...editablePrizeInfo]; // 상태 복사
                                                updatedPrizeInfo[index].amount = e.target.value; // 복사된 상태의 값을 업데이트
                                                setEditablePrizeInfo(updatedPrizeInfo); // 상태 업데이트
                                            }}
                                            style={{ width: '100%' }} // Fit the content
                                        />
                                    </CTableDataCell>
                                </CTableRow>
                            ))}
                        </CTableBody>
                    </CTable>
                </CModalBody>
                <CModalFooter>
                    <CButton
                        color="primary"
                        onClick={handleSavePrizeSettings}
                    >
                        Save
                    </CButton>
                    <CButton color="secondary" onClick={() => setEditModalVisible(false)}>
                        Close
                    </CButton>
                </CModalFooter>
            </CModal>



        </CRow>
    );
};

export default EventDetail;


