import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { CCard, CCardBody, CCardHeader, CCol, CRow } from '@coreui/react';

const EventDetail = () => {
    const { eventId } = useParams(); // URL에서 이벤트 ID를 가져옴
    const [event, setEvent] = useState(null); // 단일 이벤트 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태

    // API 호출
    const fetchEventDetail = async () => {
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/event/${eventId}`);
            if (!response.ok) {
                throw new Error('네트워크 응답이 올바르지 않습니다');
            }
            const data = await response.json();
            setEvent(data); // 단일 이벤트 데이터 업데이트
        } catch (error) {
            setError(error.message); // 에러 업데이트
        } finally {
            setLoading(false); // 로딩 완료
        }
    };

    // 컴포넌트가 마운트될 때 이벤트를 가져옴
    useEffect(() => {
        fetchEventDetail();
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
                        <strong>이벤트 상세 정보</strong>
                    </CCardHeader>
                    <CCardBody>
                        {event ? (
                            <div>
                                <p><strong>이벤트 이름:</strong> {event.eventName}</p>
                                <p><strong>시작일:</strong> {new Date(event.startAt).toLocaleString()}</p>
                                <p><strong>종료일:</strong> {new Date(event.endAt).toLocaleString()}</p>
                                <p><strong>차량명:</strong> {event.carName}</p>
                                {/* 추가적인 이벤트 정보를 여기에 표시 */}
                            </div>
                        ) : (
                            <p>이벤트 정보를 불러올 수 없습니다.</p>
                        )}
                    </CCardBody>
                </CCard>
            </CCol>
        </CRow>
    );
};

export default EventDetail;
