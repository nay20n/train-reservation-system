#API 명세

## 공통

- 주소는 모두 `/api`로 시작해요.
- 데이터는 JSON으로 주고받아요.
- 로그인하면 받는 `memberId`를 프론트가 저장해두고, 필요한 API에 같이 보내요.
- 날짜는 `2026-12-01`, 시간은 `06:00` 형식이에요.

### 에러 응답 형식

실패하면 HTTP 상태코드와 함께 아래 형식

```json
{ 
	"code": "SEAT_ALREADY_RESERVED", 
	"message": "이미 예약된 좌석입니다." 
}
```

| 상태코드 | 의미 |
| --- | --- |
| 400 | 입력값이 잘못됨 (빈 값, 형식 오류) |
| 401 | 로그인 실패 |
| 404 | 없는 데이터 |
| 409 | 처리할 수 없는 상태 (중복, 이미 예약됨 등) |

---

## API 목록

| # | 기능 | Method | URL |
| --- | --- | --- | --- |
| 1 | 회원가입 | POST | `/api/members` |
| 2 | 로그인 | POST | `/api/login` |
| 3 | 역 목록 | GET | `/api/stations` |
| 4 | 열차 조회 | GET | `/api/train-runs` |
| 5 | 좌석 조회 | GET | `/api/train-runs/{trainRunId}/seats` |
| 6 | 예약하기 | POST | `/api/reservations` |
| 7 | 결제하기 | POST | `/api/reservations/{reservationId}/payment` |
| 8 | 내 예약 목록 | GET | `/api/reservations?memberId={memberId}` |
| 9 | 예약 상세 | GET | `/api/reservations/{reservationId}` |
| 10 | 예약 취소 | POST | `/api/reservations/{reservationId}/cancel` |

**화면 흐름**: 회원가입 → 로그인 → 열차 조회 → 좌석 선택 → 예약 → 결제 → 예약 내역 → 취소

---

## 1. 회원가입

`POST /api/members`

**요청**

```json
{
  "loginId": "train01",
  "password": "1234",
  "name": "홍길동",
  "phone": "010-1234-5678"
}
```

**응답** `201`

```json
{ "memberId": 1 }
```

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 400 | `INVALID_INPUT` | 필수값이 비어 있음 |
| 409 | `DUPLICATE_LOGIN_ID` | 이미 있는 아이디 |

---

## 2. 로그인

`POST /api/login`

**요청**

```json
{ "loginId": "train01", "password": "1234" }
```

**응답** `200`

```json
{ "memberId": 1, "name": "홍길동" }
```

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 401 | `LOGIN_FAILED` | 아이디 또는 비밀번호가 틀림 |

---

## 3. 역 목록

`GET /api/stations`

**응답** `200`

```json
[
  { "stationId": 1, "stationName": "서울" },
  { "stationId": 2, "stationName": "대전" },
  { "stationId": 3, "stationName": "동대구" },
  { "stationId": 4, "stationName": "부산" }
]
```

---

## 4. 열차 조회

`GET /api/train-runs?departureStationId=1&arrivalStationId=4&date=2026-12-01`

**응답** `200`

```json
[
  {
    "trainRunId": 101,
    "trainNo": "KTX 001",
    "trainType": "KTX",
    "departureTime": "06:00",
    "arrivalTime": "08:40",
    "fares": [
      { "seatClass": "일반실", "fareAmount": 59800 },
      { "seatClass": "특실", "fareAmount": 83700 }
    ]
  }
]
```

- 조회 결과가 없으면 빈 배열 `[]`이에요.

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 400 | `SAME_STATION` | 출발역과 도착역이 같음 |

---

## 5. 좌석 조회

`GET /api/train-runs/{trainRunId}/seats`

**응답** `200`

```json
[
  { "seatId": 301, "carNo": 3, "seatNo": "1A", "seatClass": "일반실", "reserved": false },
  { "seatId": 302, "carNo": 3, "seatNo": "1B", "seatClass": "일반실", "reserved": true }
]
```

- `reserved: true`인 좌석은 선택할 수 없게 표시해주세요.

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 404 | `TRAIN_RUN_NOT_FOUND` | 없는 운행 |

---

## 6. 예약하기

`POST /api/reservations`

**요청**

```json
{
  "memberId": 1,
  "trainRunId": 101,
  "departureStationId": 1,
  "arrivalStationId": 4,
  "passengers": [
    { "passengerName": "홍길동", "birthDate": "1995-03-10", "seatId": 301 },
    { "passengerName": "김철수", "birthDate": "1996-07-21", "seatId": 303 }
  ]
}
```

- 승객 수만큼 `passengers`에 넣어요. 승객 1명당 좌석 1개예요.
- 금액은 서버가 계산해요. 프론트는 보내지 않아요.

**응답** `201`

```json
{
  "reservationId": 5001,
  "reservationCode": "R20261201-0001",
  "reservationStatus": "예약대기",
  "totalAmount": 119600
}
```

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 400 | `INVALID_INPUT` | 승객이 없거나, 같은 좌석을 두 번 선택함 |
| 404 | `TRAIN_RUN_NOT_FOUND` | 없는 운행 |
| 409 | `SEAT_ALREADY_RESERVED` | 다른 사람이 먼저 예약한 좌석 → 좌석 목록을 다시 불러와 주세요 |

---

## 7. 결제하기

`POST /api/reservations/{reservationId}/payment`

실제 결제 연동 없이, 요청이 오면 결제 완료로 처리해요.

**요청**

```json
{ "paymentMethod": "카드" }
```

- `paymentMethod`: `카드` / `계좌이체`

**응답** `201`

```json
{
  "paymentId": 3001,
  "paymentAmount": 119600,
  "paymentStatus": "결제완료",
  "reservationStatus": "예약확정"
}
```

- 결제가 끝나면 승객별 승차권이 자동으로 발급돼요.

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 404 | `RESERVATION_NOT_FOUND` | 없는 예약 |
| 409 | `ALREADY_PAID` | 이미 결제한 예약 |
| 409 | `RESERVATION_CANCELLED` | 취소된 예약 |

---

## 8. 내 예약 목록

`GET /api/reservations?memberId=1`

**응답** `200`

```json
[
  {
    "reservationId": 5001,
    "reservationCode": "R20261201-0001",
    "reservationStatus": "예약확정",
    "trainNo": "KTX 001",
    "runDate": "2026-12-01",
    "departureStationName": "서울",
    "departureTime": "06:00",
    "arrivalStationName": "부산",
    "arrivalTime": "08:40",
    "passengerCount": 2,
    "totalAmount": 119600
  }
]
```

- 예약이 없으면 빈 배열 `[]`이에요.

---

## 9. 예약 상세

`GET /api/reservations/{reservationId}`

**응답** `200`

```json
{
  "reservationId": 5001,
  "reservationCode": "R20261201-0001",
  "reservationStatus": "예약확정",
  "trainNo": "KTX 001",
  "runDate": "2026-12-01",
  "departureStationName": "서울",
  "departureTime": "06:00",
  "arrivalStationName": "부산",
  "arrivalTime": "08:40",
  "passengers": [
    { "passengerName": "홍길동", "carNo": 3, "seatNo": "1A", "seatClass": "일반실", "fareAmount": 59800, "ticketStatus": "발권" },
    { "passengerName": "김철수", "carNo": 3, "seatNo": "1C", "seatClass": "일반실", "fareAmount": 59800, "ticketStatus": "발권" }
  ],
  "payment": {
    "paymentMethod": "카드",
    "paymentAmount": 119600,
    "paymentStatus": "결제완료"
  }
}
```

- 결제 전이면 `payment`는 `null`, `ticketStatus`도 `null`이에요.

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 404 | `RESERVATION_NOT_FOUND` | 없는 예약 |

---

## 10. 예약 취소

`POST /api/reservations/{reservationId}/cancel`

요청 본문은 없어요.

**응답** `200`

```json
{
  "reservationStatus": "예약취소",
  "refundAmount": 119600
}
```

- 결제 전에 취소하면 `refundAmount`는 `0`이에요.
- 취소된 좌석은 다시 예약할 수 있어요.

**에러**

| 상태 | code | 언제 |
| --- | --- | --- |
| 404 | `RESERVATION_NOT_FOUND` | 없는 예약 |
| 409 | `ALREADY_CANCELLED` | 이미 취소된 예약 |

---

## 상태값

| 구분 | 값 |
| --- | --- |
| 예약 | 예약대기 → 예약확정 → 예약취소 |
| 결제 | 결제완료 → 환불 |
| 승차권 | 발권 → 취소 |
