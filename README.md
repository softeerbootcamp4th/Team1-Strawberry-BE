# :strawberry: Team1-Strawberry-BE

<div align="center">

| 김민준                                                                                                                    | 김민규 | 
|------------------------------------------------------------------------------------------------------------------------|---|
| [<img src="https://avatars.githubusercontent.com/u/108040422?v=4" height=150 width=150>](https://github.com/violet-mj) | [<img src="https://avatars.githubusercontent.com/u/56664567?v=4" height=150 width=150>](https://github.com/min9805) <br /> 
| [violet-mj](https://github.com/violet-mj)                                                                                                          | [min9805](https://github.com/min9805)

</div>

# 🌐 배포 URL
[Service URL](https://front.softeer1.site/)

# ✨ 기능 소개
[기능 및 시연 영상](https://www.youtube.com/watch?v=TuaAZ3bZ9uA)

## 1. 랜딩 페이지

-   신차에 대한 간략한 소개와 진행하는 이벤트의 설명을 확인할 수 있습니다.

## 2. 신차 소개 페이지

-   신차에 대한 상세한 설명을 볼 수 있는 페이지입니다.

## 3. 선착순 퀴즈 이벤트 페이지

-   선착순으로 퀴즈를 맞추는 이벤트를 진행하는 페이지입니다.
-   소개 페이지와 진행 페이지로 나뉩니다.
-   이벤트 진행 페이지에서 퀴즈를 진행한 후 바로 결과를 확인할 수 있습니다.
-   Redis 를 사용해 높은 성능으로 동시성 제어가 가능합니다.

## 4. 드로잉 이벤트 페이지

-   신차의 주요 특징을 드로잉 게임을 통해 고객에게 알릴 수 있는 페이지입니다.
-   소개 페이지, 진행 페이지, 결과 페이지로 나뉩니다.
-   소개 페이지에서 점수 랭킹과 자신의 점수를 확인할 수 있습니다.
-   게임 진행 후 일정한 알고리즘에 따라 점수를 산출해냅니다.
-   사용자는 게임 진행 후 결과를 확인하고 이를 링크 공유할 수 있습니다.

## 5. 기대평 페이지

-   신차에 대한 기대평을 작성할 수 있는 페이지입니다.
-   고객은 기대평을 작성하거나 타인의 댓글을 확인할 수 있습니다.

## 6. 어드민 페이지

- 어드민은 전체 이벤트 참여자와 실제 차량 구매자(가상)을 관리할 수 있습니다.
- 이벤트의 시작 종료 일자를 변경할 수 있습니다.
- 상세 이벤트에서 내용을 변경할 수 있습니다.
- 상세 이벤트의 당첨자 선정과 조회가 가능합니다.
- 각 상세 이벤트의 참여자들을 조회할 수 있습니다.
- 대시보드를 통해 각 이벤트 별 참여자와 실제 구매자(가상)의 비율을 확인할 수 있습니다.

# 🛠️ 개발환경

- `Java 17`
- `Spring Boot 3.3.2`
- `MySQL 8.0`, `JPA`, `Redis`
- `Github Actions` , `AWS CodeDeploy`, `AWS S3`, `AWS EC2`
- `Swagger 3.0.0`
- `Locust`

# 📄 [그라운드 룰](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%5B%EA%B7%B8%EB%9D%BC%EC%9A%B4%EB%93%9C-%EB%A3%B0%5D)

1. 퇴근 전 회고
2. PR 시 코드 리뷰
3. 칭찬 무조건 2가지 이상 ( 코드 외 가능)
4. 아쉬운 점 무조건 2가지 이상 (코드 외 가능)
5. 월요일마다 주간 리뷰
6. 막히는 부분 있으면 페어프로그래밍 하기
7. 이슈 (궁금한 점, 모르는 것 등등) 발생 시 위키 기록 후 정리 및 공유

# 🤝 협업 방식

## 1. 슬랙을 이용하여 협업

슬랙을 통해 프론트엔드와의 협업을 기록하고 빠르게 대응합니다. 

<img width="912" alt="image" src="https://github.com/user-attachments/assets/dd2d5808-21bb-42a6-9abf-c5efddd9e353">


## 2. [git flow를 이용하여 협업](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%5B%EC%BB%A8%EB%B2%A4%EC%85%98%5D)

git-flow 사용해 `feature`, `fix` 단위 작업과 `release`, `hotfix` 를 통한 배포를 진행합니다.

<img width="1128" alt="image" src="https://github.com/user-attachments/assets/03963aec-8a2f-4b3a-b4a0-21930e964073">

## 3. swagger를 이용하여 프론트 협업

Swagger 문서를 최대한 자세히 작성해 프론트엔드와의 협업을 용이하게 합니다. 
Swagger 에는 각 API 의 설명, 요청 및 모든 응답의 예시가 포함됩니다.

<img width="1431" alt="image" src="https://github.com/user-attachments/assets/77dc734b-3eb4-413f-ae75-1a465fde1390">
<img width="1427" alt="image" src="https://github.com/user-attachments/assets/1bf04d00-0d8a-4019-8e6f-62bea1754b4d">

## 4. 코드 리뷰 

그라운드 룰에 따라 최대한 자세하고 사소한 코드리뷰를 진행하고자 노력했습니다.

<img width="868" alt="image" src="https://github.com/user-attachments/assets/76c84c34-f86a-45d4-92fc-32f85d74bee2">


# 📊 아키텍쳐

## 서비스 아키텍쳐

<img width="860" alt="image" src="https://github.com/user-attachments/assets/00f26f9e-18ed-4f47-973e-180ad27cb796">

## 부하 테스트 아키텍처

<img width="849" alt="image" src="https://github.com/user-attachments/assets/44f91095-a3e2-4008-a00f-0269e3ea54d3">

## CICD 아키텍처

![image](https://github.com/user-attachments/assets/98218bc1-c169-46ee-b655-9b046e70941a)

# ⚙️ ERD 설계
![image](https://github.com/user-attachments/assets/0a883cc1-9834-4390-85df-997a3f31ba12)

# 📖 이슈 정리

## [CI/CD 환경 구축 - by min9805](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/CI-CD-%ED%99%98%EA%B2%BD-%EA%B5%AC%EC%B6%95)

Github Action, AWS S3, AWS CodeDeploy 를 활용한 CI/CD 환경 구축

## [랭킹 시스템 정리 - by min9805](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8-%E2%80%90-%EB%9E%AD%ED%82%B9-JWT-%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EC%A0%81%EC%9A%A9-(%EC%B5%9C%EC%A2%85))

DB 기반, Redis 기반 랭킹 시스템 성능 테스트 및 결론

## [대기열 도입 - by min9805](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%EB%8C%80%EA%B8%B0%EC%97%B4-%EB%8F%84%EC%9E%85)

Redis 기반 대기열 도입으로 트래픽 처리

## [N명 추첨 - by min9805](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/N%EB%AA%85-%EC%B6%94%EC%B2%A8)

이벤트 N명 추첨 방식

## [선착순 동시성 문제 - by violet-mj](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%E2%80%90-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C)

선착순 부하 시 동시성 문제 분석 및 결과

## [선착순 구현별 성능 차이 - by violet-mj](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%E2%80%90-%EA%B5%AC%ED%98%84%EC%97%90-%EB%94%B0%EB%A5%B8-%EC%84%B1%EB%8A%A5-%EB%B6%84%EC%84%9D)

mysql, redis, batch 작업에 따른 성능 차이를 비교

## [스레드 풀 조정 - by violet-mj](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%E2%80%90-%EC%8A%A4%EB%A0%88%EB%93%9C-%ED%92%80-%EB%B3%80%EA%B2%BD%EC%97%90-%EB%94%B0%EB%A5%B8-%EC%84%B1%EB%8A%A5-%EC%B0%A8%EC%9D%B4)

스레드 풀 개수에 따른 동시 처리 능력 분석

# 📝 [[회고]](https://github.com/softeerbootcamp4th/Team1-Strawberry-BE/wiki/%5B%ED%9A%8C%EA%B3%A0%5D)
