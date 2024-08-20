INSERT INTO prizes (id, product_name, price, prize_img_url, prize_winning_img_url)
VALUES (1, '스타벅스 상품권', 4000, 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner1.svg',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner1.svg'),
       (2, '자전거', 120000, 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner2.svg',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner2.svg'),
       (3, '아이폰', 1000000, 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner3.svg',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/winner3.svg');

INSERT INTO users (id, email, name, phone_number, birth_date, o_auth_provider)
VALUES (1, 'alswnssl0528@naver.com', '김민준', '010-6833-4234', '1998-05-28', 0),
       (2, 'seong123@naver.com', '김승준', '010-1234-4234', '2000-03-28', 0),
       (3, 'sarang@naver.com', '한사랑', '010-5432-4234', '1999-11-28', 0);

INSERT INTO cars (id, brand_name, car_name_eng, car_name_kor, model_line, price, release_date)
VALUES (1, 0, 'santafe', '산타페', 0, 30000000, '2024-07-01 10:00:00');

INSERT INTO events (id, end_at, event_name, event_status, start_at, winner_count, car_id, event_img_urls,
                    expectation_banner_img_url)
VALUES (1, '2024-09-01 10,00:00', '별 헤는 밤', 0, '2024-07-25 10:00:00', 10, 1, JSON_OBJECT(
        "mainImgUrl", "www.mainimg.com",
        "scrolledImgUrl", "www.scroll.com",
        "eventInfoImg", "www.eventinfo.com",
        "quizMainImg", "www.quizmain.com",
        "quizPrizeImg", "www.qprize.com",
        "drawingMainImg", "www.drawingmain.com",
        "drawingPrizeImg", "www.drawingprize.com"),
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/expectationBG.svg'),
        (2, '2024-09-01 10,00:00', '별 헤는 밤', 0, '2024-09-30 10:00:00', 10, 1, JSON_OBJECT(
        "mainImgUrl", "www.mainimg.com2",
        "scrolledImgUrl", "www.scroll.co2m",
        "eventInfoImg", "www.eventinfo.co2m",
        "quizMainImg", "www.quizmain.com2",
        "quizPrizeImg", "www.qprize.com2",
        "drawingMainImg", "www.drawingmainw.com",
        "drawingPrizeImg", "www.drawingpriz2e.com"),
        'https://softeer-static.s3.ap-northeast-4.amazonaws.com/expectationBG.svg');
INSERT INTO sub_events (id, event_id, alias, execute_type, event_type, start_at, end_at, banner_img_url, event_img_urls,
                        winners_meta)
VALUES (1, 1, '퀴즈 테스트', 1, 1, '2024-08-20 10:30:00', '2024-08-22 10:30:00',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_banner.svg',
        JSON_OBJECT('main', 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_info.svg'),
        JSON_OBJECT('1', JSON_OBJECT('prizeId', 1, 'winnerCount', 1))),
       (2, 1, 'ㅣ히히', 1, 1, '2024-08-23 10:30:00', '2024-08-24 10:30:00',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_banner.svg',
        JSON_OBJECT('main', 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_info.svg'),
        JSON_OBJECT('1', JSON_OBJECT('prizeId', 1, 'winnerCount', 1))),
       (3, 1, '퀴즈 시러', 1, 1, '2024-08-25 10:30:00', '2024-08-30 10:30:00',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_banner.svg',
        JSON_OBJECT('main', 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/event_info.svg'),
        JSON_OBJECT('1', JSON_OBJECT('prizeId', 1, 'winnerCount', 1))),
       (4, 1, '드로잉', 0, 0, '2024-08-01 10:30:00', '2024-09-30 10:30:00',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/drawingEvent_banner.svg',
        JSON_OBJECT('how', 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/drawingEvent_how.svg',
                    'reward',
                    'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/drawingEvent_reward.svg',
                    'description',
                    'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/drawingEvent_discription.svg'),
        JSON_OBJECT('1', JSON_OBJECT('prizeId', 1, 'winnerCount', 1)));

INSERT INTO quiz_firstcome_events (sequence, sub_event_id, prize_id, anchor, answer, hint, problem, winners,
                                   winner_count,
                                   init_consonant, car_info)
VALUES (1, 1, 1, '#sub1', '10.4', '10 근처', '산타페의 연비는?', 2, 1, 'ㅅㅈㅅ', '산타페의 선루프는....'),
       (2, 2, 2, '#sub2', '11.5', '11 근처', '산타페의 연비는?', 3, 3, 'ㅅㅇㅈㅇ', '산타페의 엔진은..'),
       (3, 3, 3, '#sub3', '12.5', '12 근처', '산타페의 연비는?', 12, 9, 'ㅅㅇㅈㅇ', '산타페의 가격은..');

INSERT INTO winners (id, prize_id, sub_event_id, user_id, ranking)
VALUES (1, 1, 3, 1, 1),
       (2, 2, 2, 2, 2),
       (3, 3, 1, 3, 3);

INSERT INTO expectations (id, event_id, user_id, expectation_comment, created_at, modified_at)
VALUES (1, 1, 1, '기대됩니다.', '2024-07-01 10:00:00', '2024-07-01 10:00:00'),
       (2, 1, 2, '게임 재밌어요!', '2024-07-02 10:00:00', '2024-07-02 10:00:00'),
       (3, 1, 3, '언제 나와요.', '2024-07-03 10:00:00', '2024-07-03 10:00:00'),
       (4, 1, 1, '우우우1', '2024-07-05 10:00:00', '2024-07-04 10:00:00'),
       (5, 1, 1, '우우우2', '2024-07-06 10:00:00', '2024-07-04 10:00:00'),
       (6, 1, 1, '우우우3', '2024-07-07 10:00:00', '2024-07-04 10:00:00'),
       (7, 1, 1, '우우우4', '2024-07-08 10:00:00', '2024-07-04 10:00:00'),
       (8, 1, 1, '우우우5', '2024-07-09 10:00:00', '2024-07-04 10:00:00'),
       (9, 1, 1, '우우우6', '2024-07-10 10:00:00', '2024-07-04 10:00:00'),
       (10, 1, 1, '우우우7', '2024-07-11 10:00:00', '2024-07-04 10:00:00'),
       (11, 1, 1, '우우우8', '2024-07-12 10:00:00', '2024-07-04 10:00:00'),
       (12, 1, 2, '우우우9', '2024-07-13 10:00:00', '2024-07-04 10:00:00'),
       (13, 1, 3, '우우우10', '2024-07-14 10:00:00', '2024-07-04 10:00:00');

INSERT INTO event_users (id, is_write_expectation, last_visited_at, last_charge_at, shared_url, shared_score,
                         priority_Score,
                         lotto_Score,
                         game_Score, chance, expectation_Bonus_Chance, share_Bonus_Chance, user_id, sub_event_id)
VALUES (1, 1, '2024-07-01 10:00:00', '2024-07-01 10:00:00', 'dmSi3m', 0, 0, 0, 0, 0, 0, 0, 1, 1),
       (2, 1, '2024-07-02 10:00:00', '2024-07-02 10:00:00', 'DoId2R', 0, 0, 0, 0, 0, 0, 0, 2, 1),
       (3, 1, '2024-07-03 10:00:00', '2024-07-03 10:00:00', 'NAkv3r', 0, 0, 0, 0, 0, 0, 0, 3, 1);

INSERT INTO drawing_lottery_events (id, sub_event_id, alias, sequence, contour_img_url, img_url, draw_points_json_url,
                                    start_pos_x, start_pos_y, blur_img_url, result_detail, on_boarding_msg, play_msg)
VALUES (1, 4, "1차 드로잉", 1,
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/01_contour_img.png',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/01_img.png', "www.draw1.com", 54,
        275, 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/01_blur_img.png',
        '디 올 뉴 싼타페의 실내는 언제 어디서든 아웃도어 라이프를 즐길 수 있는 넉넉한 거주 공간을 자랑하며\n 수평과 수직 이미지를 강조한 레이아웃으로 외장과 자연스럽게 어우러질 수 있도록 디자인했습니다.',
        '3초 후 디 올 뉴 싼테파를 그려주세요!', '7초 안에 선을 따라 디 올 뉴 싼타페를 그려주세요!'),
       (2, 4, "2차 드로잉", 2,
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/02_contour_img.png',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/02_img.png', "www.draw2.com", 318,
        238, 'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/03_blur_img.png',
        '디 올 뉴 싼타페는 평평하게 폴딩이 가능한 2/3열 시트와 넓은 테일게이트 구성으로\n 가장 실용적인 SUV로 나아가고 있습니다.', '3초 후 디 올 뉴 싼테파를 그려주세요!',
        '7초 안에 선을 따라 디 올 뉴 싼타페를 그려주세요!'),
       (3, 4, "3차 드로잉", 3,
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/03_contour_img.png',
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/03_img.png', "www.contour3.com",
        240, 216,
        'https://softeer-static.s3.ap-northeast-2.amazonaws.com/drawingLottery/game/03_blur_img.png',
        '디 올 뉴 싼타페는 테라스 컨셉의 테일게이트 공간을 기반으로 적재 이용성은 물론\n 대형 테일게이트의 개방감을 활용한 다양한 아웃도어 활동을 지원합니다.',
        '3초 후 디 올 뉴 싼테파를 그려주세요!', '7초 안에 선을 따라 디 올 뉴 싼타페를 그려주세요!');
