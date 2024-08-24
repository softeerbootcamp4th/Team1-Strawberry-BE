// import React, { useEffect, useState } from 'react';
// import { useParams } from 'react-router-dom';
// import {
//     CCard,
//     CCardBody,
//     CCardHeader,
//     CCol,
//     CRow,
//     CTable,
//     CTableBody,
//     CTableCaption,
//     CTableDataCell,
//     CTableHead,
//     CTableHeaderCell,
//     CTableRow,
//     CButton,
//     CModal,
//     CModalHeader,
//     CModalTitle,
//     CModalBody,
//     CModalFooter,
//   } from '@coreui/react'
  
// const EventDetail = () => {
//     const { eventId } = useParams(); // URL에서 이벤트 ID를 가져옴
//     const [events, setEvents] = useState(null); // 단일 이벤트 상태
//     const [winners, setWinners] = useState(null); // 단일 이벤트 상태
//     const [loading, setLoading] = useState(true); // 로딩 상태
//     const [error, setError] = useState(null); // 에러 상태
//     const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
//     const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
//     const [visible, setVisible] = useState(false)

//     // API 호출
//     const fetchEventDetails = async (id = eventId, page = 0, size = 10) => {
//         try {
//             const response = await fetch(`http://localhost:8080/api/v1/subevent/list?eventId=${id}&page=${page}&size=${size}`);
//             if (!response.ok) {
//                 throw new Error('네트워크 응답이 올바르지 않습니다');
//             }
//             const data = await response.json();
//             setEvents(data.content); // 이벤트 리스트 업데이트
//             setTotalPages(data.totalPages); // 총 페이지 수 업데이트
//             console.log(data);
//         } catch (error) {
//             setError(error.message); // 에러 업데이트
//         } finally {
//             setLoading(false); // 로딩 완료
//         }
//     };

//     // API 호출
//     const fetchWinners = async (subEventId) => {
//         try {
//             const response = await fetch(`http://localhost:8080/api/v1/subevent/winner?subEventId=${subEventId}`);
//             if (!response.ok) {
//                 throw new Error('네트워크 응답이 올바르지 않습니다');
//             }
//             const data = await response.json();
//             setWinners(data.data); // 이벤트 리스트 업데이트
//             console.log(data);
//         } catch (error) {
//             setError(error.message); // 에러 업데이트
//         } finally {
//             setLoading(false); // 로딩 완료
//         }
//     };

//     const handleViewParticipants = (subEventId) => {
//         window.location.href = `/#/event/` + eventId + `/` + subEventId; // 페이지 이동
//     };

//     // 컴포넌트가 마운트될 때 이벤트를 가져옴
//     useEffect(() => {
//         fetchEventDetails();
//     }, [eventId]); // eventId가 변경될 때마다 호출

//     if (loading) {
//         return <div>로딩 중...</div>;
//     }

//     if (error) {
//         return <div>에러 발생: {error}</div>;
//     }

//     return (
//         <CRow>
//             <CCol xs={12}>
//                 <CCard className="mb-4">
//                     <CCardHeader>
//                         <strong>이벤트 리스트</strong>
//                     </CCardHeader>
//                     <CCardBody>
//                         <CTable>
//                             <CTableHead>
//                                 <CTableRow>
//                                     <CTableHeaderCell>이벤트 이름</CTableHeaderCell>
//                                     <CTableHeaderCell>시작일</CTableHeaderCell>
//                                     <CTableHeaderCell>종료일</CTableHeaderCell>
//                                     <CTableHeaderCell>이벤트 종류</CTableHeaderCell>
//                                     <CTableHeaderCell>추첨 종류</CTableHeaderCell>
//                                     <CTableHeaderCell>당첨자 조회</CTableHeaderCell>
//                                     <CTableHeaderCell>당첨자 추첨</CTableHeaderCell>
//                                     <CTableHeaderCell>이벤트 수정</CTableHeaderCell>
//                                     <CTableHeaderCell>당첨자 추첨</CTableHeaderCell>
//                                     <CTableHeaderCell>참여자 조회</CTableHeaderCell>
//                                 </CTableRow>
//                             </CTableHead>
//                             <CTableBody>
//                                 {events.map((event) => (
//                                     <CTableRow key={event.id}>
//                                         <CTableDataCell>{event.alias}</CTableDataCell>
//                                         <CTableDataCell>{new Date(event.startAt).toLocaleString()}</CTableDataCell>
//                                         <CTableDataCell>{new Date(event.endAt).toLocaleString()}</CTableDataCell>
//                                         <CTableDataCell>{event.subEventExecuteType}</CTableDataCell>
//                                         <CTableDataCell>{event.subEventType}</CTableDataCell>
//                                         <CTableDataCell>
//                                             <CButton
//                                                 color="primary"
//                                                 onClick={() => {
//                                                     setVisible(true); // Show the modal
//                                                     fetchWinners(event.id); // Pass the subEventId to fetchWinners
//                                                 }}
//                                                 >조회</CButton>
//                                             <CModal
//                                             visible={visible}
//                                             onClose={() => setVisible(false)}
//                                             aria-labelledby="LiveDemoExampleLabel"
//                                             >
//                                             <CModalHeader>
//                                                 <CModalTitle id="LiveDemoExampleLabel">Winners List</CModalTitle>
//                                             </CModalHeader>
//                                             <CModalBody>
//                                                 <CTableBody>
//                                                     {winners && winners.map((winner) => (
//                                                     <CTable>
//                                                         <CTableHead>
//                                                           <CTableRow>
//                                                             <CTableHeaderCell>Name</CTableHeaderCell>
//                                                             <CTableHeaderCell>Prize</CTableHeaderCell>
//                                                             <CTableHeaderCell>Rank</CTableHeaderCell>
//                                                           </CTableRow>
//                                                         </CTableHead>
//                                                         <CTableBody>
//                                                           {winners.map((winner, index) => (
//                                                             <CTableRow key={index}>
//                                                               <CTableDataCell>{winner.userId}</CTableDataCell>
//                                                               <CTableDataCell>{winner.prizeName}</CTableDataCell>
//                                                               <CTableDataCell>{winner.ranking}</CTableDataCell>
//                                                             </CTableRow>
//                                                           ))}
//                                                         </CTableBody>
//                                                       </CTable>
//                                                     ))}
//                                                 </CTableBody>
//                                             </CModalBody>
//                                             <CModalFooter>
//                                                 <CButton color="secondary" onClick={() => setVisible(false)}>
//                                                 Close
//                                                 </CButton>
//                                             </CModalFooter>
//                                             </CModal>
//                                         </CTableDataCell>
//                                         <CTableDataCell>
//                                             <CButton
//                                                 color="success"
//                                                 onClick={() => handleViewDetails(event.id)}
//                                                 disabled={event.subEventExecuteType === 'FIRSTCOME'}
//                                             >
//                                                 추첨
//                                             </CButton>
//                                         </CTableDataCell>
//                                         <CTableDataCell>
//                                             <CButton
//                                                 color="light"
//                                                 onClick={() => handleViewParticipants(event.id)}
//                                             >
//                                                 조회
//                                             </CButton>
//                                         </CTableDataCell>
//                                     </CTableRow>
//                                 ))}
//                             </CTableBody>
//                         </CTable>
//                         <div className="mt-3">
//                             {Array.from({ length: totalPages }, (_, index) => (
//                                 <button
//                                     key={index}
//                                     onClick={() => handlePageChange(index)}
//                                     disabled={currentPage === index} // 현재 페이지는 비활성화
//                                     className="btn btn-primary mx-1"
//                                 >
//                                     {index + 1}
//                                 </button>
//                             ))}
//                         </div>
//                     </CCardBody>
//                 </CCard>
//             </CCol>
//         </CRow>
//     );
// };

// export default EventDetail;


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
    const [selectedEvent, setSelectedEvent] = useState(null); // 선택한 이벤트 상태

    // 이벤트 상세정보 API 호출
    const fetchEventDetails = async (id = eventId, page = 0, size = 10) => {
        setLoading(true); // 로딩 상태 설정
        try {
            const response = await fetch(`http://localhost:8080/api/v1/subevent/list?eventId=${id}&page=${page}&size=${size}`);
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
            const response = await fetch(`http://localhost:8080/api/v1/subevent/winner?subEventId=${subEventId}`);
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
            const response = await fetch(`http://localhost:8080/api/v1/event/${selectedEvent.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(selectedEvent),
            });

            if (!response.ok) {
                throw new Error('Failed to update event');
            }

            // Refresh the events list after a successful update
            fetchEventDetails(eventId, currentPage);
            setEditModalVisible(false); // Close the edit modal
        } catch (error) {
            setError(error.message); // Display the error message
        }
    };

    const handleViewParticipants = (subEventId) => {
        window.location.href = `/#/event/` + eventId + `/` + subEventId; // 페이지 이동
    };

    // 이벤트 수정 버튼 핸들러
    const handleModify = (event) => {
        setSelectedEvent(event); // 수정할 이벤트 선택
        setEditModalVisible(true); // 수정 모달 표시
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
                                                onClick={() => handleViewParticipants(event.id)}
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

            {/* 수정 모달 */}
            <CModal
                visible={editModalVisible}
                onClose={() => setEditModalVisible(false)}
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
                                    <CTableHeaderCell>추첨 인원</CTableHeaderCell>
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
                    <CButton color="secondary" onClick={() => setEditModalVisible(false)}>
                        닫기
                    </CButton>
                </CModalFooter>
            </CModal>

            <CModal
                visible={visible}
                onClose={() => setVisible(false)}
                aria-labelledby="LiveDemoExampleLabel"
                 className="modal-lg"
                // style={{ width: 'fit-content' }}
            >
                <CModalHeader>
                    <CModalTitle id="LiveDemoExampleLabel">Winners List</CModalTitle>
                </CModalHeader>
                <CModalBody>
                    <CTable>
                        <CTableHead>
                            <CTableRow>
                                <CTableHeaderCell>Name</CTableHeaderCell>
                                <CTableHeaderCell>Prize</CTableHeaderCell>
                                <CTableHeaderCell>Rank</CTableHeaderCell>
                            </CTableRow>
                        </CTableHead>
                        <CTableBody>
                            {winners && winners.map((winner, index) => (
                                <CTableRow key={index}>
                                    <CTableDataCell>
                                        <input
                                            type="text"
                                            value={winner.userId}
                                            onChange={(e) => {
                                                const updatedWinners = [...winners];
                                                updatedWinners[index].userId = e.target.value;
                                                setWinners(updatedWinners);
                                            }}
                                        />
                                    </CTableDataCell>
                                    <CTableDataCell>
                                        <input
                                            type="text"
                                            value={winner.prizeName}
                                            onChange={(e) => {
                                                const updatedWinners = [...winners];
                                                updatedWinners[index].prizeName = e.target.value;
                                                setWinners(updatedWinners);
                                            }}
                                        />
                                    </CTableDataCell>
                                    <CTableDataCell>
                                        <input
                                            type="text"
                                            value={winner.ranking}
                                            onChange={(e) => {
                                                const updatedWinners = [...winners];
                                                updatedWinners[index].ranking = e.target.value;
                                                setWinners(updatedWinners);
                                            }}
                                        />
                                    </CTableDataCell>
                                </CTableRow>
                            ))}
                        </CTableBody>
                    </CTable>
                </CModalBody>
                <CModalFooter>
                    <CButton color="primary" onClick={handleModify}>
                        Modify
                    </CButton>
                    <CButton color="secondary" onClick={() => setVisible(false)}>
                        Close
                    </CButton>
                </CModalFooter>
            </CModal>

        </CRow>
    );
};

export default EventDetail;
