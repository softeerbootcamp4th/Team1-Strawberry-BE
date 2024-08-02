INSERT INTO Prizes (product_name, price, prize_img_url)
values ("스타벅스 상품권", 4000, "www.starbucks.com"),
       ("자전거", 120000, "www.bicycle.com"),
       ("아이폰", 1000000, "www.apple.com");

INSERT INTO Users (email, name, phone_number, birth_date, o_auth_provider)
value ("alswnssl0528@naver.com", "김민준", "010-6833-4234", "1998-05-28", 0),
      ("seong123@naver.com", "김승준", "010-1234-4234", "2000-03-28", 0),
      ("sarang@naver.com", "한사랑", "010-5432-4234", "1999-11-28", 0);

INSERT INTO cars (id, brand_name, car_name_eng, car_name_kor, model_line, price, release_date)
values (1, 0, "santafe", "산타페", 0, 30000000, "2024-07-01 10:00:00");

INSERT INTO events (id, end_at, event_name, event_registered_at, event_status, start_at, winner_count, car_id)
values (1, "2024-07-01 10:00:00", "별 헤는 밤", "2024-06-28 00:00:00", 0, "2024-06-25 10:00:00", 10, 1);

INSERT INTO sub_events (id, event_id, alias, execute_type, event_type, start_at, end_at, banner_url, event_img_url)
values (1, 1, "퀴즈 테스트", 1, 1, "2024-06-25 10:30:00", "2024-06-26 10:30:00", "www.banner1.com", "www.event1.com"),
       (2, 1, "ㅣ히히", 1, 1, "2024-06-27 10:30:00", "2024-06-28 10:30:00", "www.banner2.com", "www.event2.com"),
       (3, 1, "퀴즈 시러", 1, 1, "2024-06-29 10:30:00", "2024-06-30 10:30:00", "www.banner3.com", "www.event3.com");

INSERT INTO Quizzes (sequence, sub_event_id, prize_id, anchor, answer, hint, problem, winners, winner_count, init_consonant, car_info)
values (1, 1, 1, "#sub1", "10.4", "10 근처", "산타페의 연비는?", 2, 1, "ㅅㅈㅅ", "산타페의 선루프는...."),
       (2, 2, 2, "#sub2", "11.5", "11 근처", "산타페의 연비는?", 3, 3, "ㅅㅇㅈㅇ", "산파페의 엔진은.."),
       (3, 3, 3, "#sub3", "12.5", "12 근처", "산타페의 연비는?", 12, 9, "ㅅㅇㅈㅇ", "산타페의 가격은..");

INSERT INTO Winners (prize_id, sub_event_id, user_id)
values (1, 3, 1),
       (2, 2, 2),
       (3, 1, 3);