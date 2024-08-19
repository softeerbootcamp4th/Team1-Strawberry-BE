package com.hyundai.softeer.backend.domain.firstcome.quiz.service;

class QuizFirstComeServiceTest {

//    @InjectMocks
//    QuizFirstComeService quizFirstComeService;
//
//    @Mock
//    SubEventRepository subEventRepository;
//
//    @Mock
//    QuizFirstComeRepository quizFirstComeRepository;
//
//    @Mock
//    EventRepository eventRepository;
//
//    @Mock
//    QuizWinnerDraw quizWinnerDraw;
//
//    @Mock
//    PrizeRepository prizeRepository;
//
//    @Mock
//    DateUtil dateUtil;
//
//    @Mock
//    Clock clock;
//
//    @Test
//    @DisplayName("quiz 테스트: 퀴즈를 받아오는데 성공한 경우")
//    void getQuizSuccessTest() {
//        // given
//        QuizFirstComeRequest quizFirstComeRequest = new QuizFirstComeRequest(1L);
//
//        QuizFirstCome quizFirstCome1 = quizGenerator(1L);
//        SubEvent subEvent1 = SubEvent.subEventGenerator(1L);
//
//        Instant fixedInstant = Instant.parse("2024-06-25T00:00:00Z");
//        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
//
//        when(quizFirstComeRepository.findBySubEventId(any(Long.class))).thenReturn(Optional.of(quizFirstCome1));
//        when(subEventRepository.findById(any(Long.class))).thenReturn(Optional.of(subEvent1));
//
//        // when
//        QuizFirstComeResponseDto quizFirstComeResponseDto = quizFirstComeService.getQuiz(quizFirstComeRequest);
//
//        // then
//        assertThat(quizFirstComeResponseDto).isNotNull();
//        assertThat(quizFirstComeResponseDto.getCarInfo()).isEqualTo("산타페는 어쩌구저쩌구1");
//        assertThat(quizFirstComeResponseDto.getInitConsonant()).isEqualTo("ㅅㅇㅈㅇ1");
//        assertThat(quizFirstComeResponseDto.getSubEventId()).isEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("quiz 테스트: 이벤트 시간이 아닌 경우")
//    void getQuizNotWithinEventTest() {
//        // given
//        QuizFirstComeRequest quizFirstComeRequest = new QuizFirstComeRequest(1L);
//
//        QuizFirstCome quizFirstCome1 = quizGenerator(1L);
//        SubEvent subEvent1 = SubEvent.subEventGenerator(1L);
//
//        Instant fixedInstant = Instant.parse("2024-07-03T00:00:00Z");
//        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
//        when(clock.instant()).thenReturn(fixedInstant);
//        when(clock.getZone()).thenReturn(fixedClock.getZone());
//
//        when(quizFirstComeRepository.findBySubEventId(any(Long.class))).thenReturn(Optional.of(quizFirstCome1));
//        when(subEventRepository.findById(any(Long.class))).thenReturn(Optional.of(subEvent1));
//
//
//        // when
//        Assertions.assertThatThrownBy(() -> {
//            quizFirstComeService.getQuiz(quizFirstComeRequest);
//        }).isInstanceOf(SubEventNotWithinPeriodException.class);
//    }
//
//    @Test
//    @DisplayName("랜딩 페이지 api: 정상 반환인 경우")
//    void landingPageSuccessTest() {
//        // given
//        Long eventId = 1L;
//
//        Instant fixedInstant = Instant.parse("2024-06-25T10:00:00Z");
//        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
//        when(clock.instant()).thenReturn(fixedInstant);
//        when(clock.getZone()).thenReturn(fixedClock.getZone());
//
//        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(Event.testEventGenerator(eventId)));
//        when(subEventRepository.findByEventIdAndExecuteType(any(Long.class), any(SubEventExecuteType.class))).thenReturn(SubEvent.subEventsGenerator(3L));
//        when(quizFirstComeRepository.findBySubEventId(any(Long.class))).thenReturn(Optional.ofNullable(quizGenerator(1L)));
//
//        // when
//        QuizFirstComeLandResponseDto quizFirstComeLandResponseDto = quizFirstComeService.getQuizLand(eventId);
//
//        // then
//        assertThat(quizFirstComeLandResponseDto.getLastQuizNumber()).isEqualTo(3);
//        assertThat(quizFirstComeLandResponseDto.getHint()).isEqualTo("ㅎㅇ1");
//        assertThat(quizFirstComeLandResponseDto.isValid()).isTrue();
//    }
//
//    private Prize prizeGenerator(long i) {
//        return Prize.builder().price((int) (1000 + i)).id((Long) i).prizeImgUrl("www.prize" + i + ".com").productName("python" + i).build();
//    }
//
//    private QuizFirstCome quizGenerator(long i) {
//        return QuizFirstCome.builder()
//                .sequence((int) i)
//                .overview("산타페는 일상의 머시기" + i)
//                .problem("산타페의 연비는?")
//                .carInfo("산타페는 어쩌구저쩌구" + i)
//                .answer("답" + i)
//                .hint("ㅎㅇ" + i)
//                .anchor("#hi" + i)
//                .initConsonant("ㅅㅇㅈㅇ" + i)
//                .prize(prizeGenerator(i))
//                .winnerCount((int) i)
//                .winners((int) i + 1)
//                .subEventId(i)
//                .build();
//    }
}