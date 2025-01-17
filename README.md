# 콘서트 예약 서비스

## 요구사항
- **대기열 시스템**을 구축하여 작업 가능한 유저만 예약 서비스를 이용할 수 있도록 합니다.
- 사용자는 좌석 예약 시 **미리 충전한 잔액**을 사용합니다.
- 좌석 예약 요청 시, 결제가 이루어지지 않더라도 일정 시간 동안 **다른 유저가 해당 좌석에 접근하지 못하도록** 해야 합니다.

---

## 주요 기능

### 1. 유저 대기열 토큰 기능
- 서비스를 이용할 수 있는 **토큰을 발급**받는 기능입니다.
- 토큰은 **유저 UUID**와 **대기열 순번**을 포함합니다.
- 모든 API는 **대기열 검증**을 통과한 토큰이 있어야 이용 가능합니다.
- 대기열 상태는 기본적으로 **폴링 방식**으로 확인합니다.

#### 구현 방향
1. **토큰 발급 신청**
    - 사용자가 이름과 전화번호를 입력하여 **토큰 발급을 신청**합니다.
    - 이후 **대기열에 추가**되고, 대기열 순번을 받습니다.
2. **대기열 순번 도달**
    - 대기열 순번이 되면 토큰이 발급됩니다.
3. **진입 가능 인원 제한**
    - 최대 진입 가능 인원은 100명으로 제한합니다.
4. **토큰 만료**
    - 발급된 토큰의 만료 시간은 **30분**입니다.
    - **좌석 예약이 만료**되면 토큰도 만료됩니다.
5. **대기열 검증**
    - 모든 API는 **ACTIVE 상태**의 토큰을 가지고 있어야만 접근할 수 있습니다.

---

### 2. 예약 가능 날짜 조회
- 예약 가능한 **날짜**를 조회합니다.

---

### 3. 예약 가능 좌석 조회
- 날짜별로 예약 가능한 **좌석 목록**을 조회합니다.

---

### 4. 좌석 예약
- 사용자가 **좌석을 예약**합니다.
- **제한 사항**:
    - 예약 후 **5분 안에 결제**가 이루어지지 않으면 예약이 취소됩니다.
    - 예약 시, **해당 좌석은 임시로 다른 유저에게 접근 불가** 상태가 됩니다.
---

### 5. 잔액 충전
- 사용자의 **잔액을 충전**합니다.

---

### 6. 잔액 조회
- 사용자의 **잔액 정보를 조회**합니다.

---

### 7. 결제 API
- **좌석 예약을 위한 결제** 기능입니다.
- **결제 완료 시**:
    - 좌석을 해당 유저에게 배정합니다.
    - 토큰을 **만료 상태**로 변경합니다.
---

## 마일스톤

### 기간
- 2024.12.28 ~ 2025.01.23

### Man/Day
- 하루 3시간
- 5/week
- 20/month

### 작업 목록
1. **요구사항 분석** (0.5M)
2. **ERD 설계** (1M)
3. **시퀀스 다이어그램 작성** (1M)
4. **API 명세서 작성** (0.5M)
5. **유저 대기열 토큰 기능**  (2M)
6. **예약 가능 날짜 조회** (1M)
7. **예약 가능 좌석 조회** (1M)
8. **좌석 예약** (2M)
9. **잔액 충전** (1M)
10. **잔액 조회** (1M)
11. **결제 API** (1M)
12. **레디스로 대기열 관리** (3M)
13. **카프카 연동** (3M)
14. **배포** (1M)

![img.png](docs/img.png)

###  시퀀스 다이어그램

1. 대기열 토큰
![token-sequence.png](docs/token-sequence.png)
2. 예약 가능 날짜 조회
![get-available-date-sequence.png](docs/get-available-date-sequence.png)
 
3. 예약 가능 좌석 조회
![get-available-seats-sequence.png](docs/get-available-seats-sequence.png)

4. 좌석 예약 후 결제
![reservation-sequence.png](docs/reservation-sequence.png)
