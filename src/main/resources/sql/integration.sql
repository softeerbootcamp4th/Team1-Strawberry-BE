INSERT INTO cars (id, brand_name, car_name_eng, car_name_kor, model_line, price, release_date)
values (1, 0, "santafe", "산타페", 0, 30000000, "2024-07-01 10:00:00");

INSERT INTO events (id, end_at, event_name, event_registered_at, event_status, start_at, winner_count, car_id)
values (1, "2024-07-01 10:00:00", "별 헤는 밤", "2024-06-28 00:00:00", 0, "2024-06-25 10:00:00", 10, 1);

INSERT INTO sub_events (id, event_id, alias, execute_type, event_type, start_at, end_at, banner_url, event_img_url)
values (1, 1, "퀴즈 테스트", 1, 1, "2024-07-25 10:30:00", "2024-07-26 10:30:00", "www.banner1.com", "www.event1.com"),
       (2, 1, "ㅣ히히", 1, 1, "2024-07-27 10:30:00", "2024-07-28 12:30:00", "www.banner2.com", "www.event2.com"),
       (3, 1, "퀴즈 시러", 1, 1, "2024-07-29 10:30:00", "2024-07-30 10:30:00", "www.banner3.com", "www.event3.com");

INSERT INTO Quizzes (sequence, sub_event_id, anchor, answer, hint, problem, winners, winners_meta, init_consonant)
values (1, 1, "#sub1", "10.4", "10 근처", "산타페의 연비는?", "winnerinfo", "{1:1, 2:2, 3:3}", "ㅅㅈㅅ"),
       (2, 2, "#sub2", "11.5", "11 근처", "산타페의 연비는?", "winnerinfo", "{1:2, 2:3, 3:4}", "ㅅㅇㅈㅅ"),
       (3, 3, "#sub3", "12.5", "12 근처", "산타페의 연비는?", "winnerinfo", "{1:3, 2:4, 3:5}", "ㅅㅈㅅ");
