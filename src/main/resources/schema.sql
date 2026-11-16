-- =====================================================================
-- 기차 예약 시스템 스키마 (MySQL 8.4)
-- 기준: 테이블 정의서 (2026-11-12)
-- 서버 시작 시 자동 실행됨 (spring.sql.init.mode=always)
-- 테이블이 이미 있으면 건너뜀 → 구조 변경 시 해당 테이블 DROP 후 재시작
-- FK 때문에 생성 순서가 중요함 (참조되는 테이블이 먼저)
-- =====================================================================

-- 회원 (Member)
CREATE TABLE IF NOT EXISTS member (
    member_id      INT          NOT NULL AUTO_INCREMENT COMMENT '회원 식별자',
    login_id       VARCHAR(50)  NOT NULL COMMENT '로그인 ID',
    password       VARCHAR(255) NOT NULL COMMENT '비밀번호',
    name           VARCHAR(50)  NOT NULL COMMENT '회원이름',
    phone          VARCHAR(20)  NULL     COMMENT '연락처',
    member_status  VARCHAR(20)  NOT NULL COMMENT '회원상태',
    PRIMARY KEY (member_id),
    UNIQUE KEY uq_member_login_id (login_id)
) COMMENT '회원';

-- 역 (Station)
CREATE TABLE IF NOT EXISTS station (
    station_id    INT          NOT NULL AUTO_INCREMENT COMMENT '역 식별자',
    station_name  VARCHAR(50)  NOT NULL COMMENT '역 이름',
    city          VARCHAR(50)  NULL     COMMENT '도시/지역',
    address       VARCHAR(200) NULL     COMMENT '역 주소',
    PRIMARY KEY (station_id)
) COMMENT '역';

-- 기차 (Train)
CREATE TABLE IF NOT EXISTS train (
    train_id    INT         NOT NULL AUTO_INCREMENT COMMENT '열차 식별자',
    train_no    VARCHAR(20) NOT NULL COMMENT '열차번호',
    train_type  VARCHAR(30) NOT NULL COMMENT '열차종류',
    PRIMARY KEY (train_id),
    UNIQUE KEY uq_train_no (train_no)
) COMMENT '기차';

-- 좌석 (Seat)
CREATE TABLE IF NOT EXISTS seat (
    seat_id      INT         NOT NULL AUTO_INCREMENT COMMENT '좌석 식별자',
    train_id     INT         NOT NULL COMMENT '열차 식별자',
    car_no       INT         NOT NULL COMMENT '호차 번호',
    seat_no      VARCHAR(10) NOT NULL COMMENT '좌석 번호',
    seat_class   VARCHAR(20) NOT NULL COMMENT '좌석 등급',
    seat_status  VARCHAR(20) NOT NULL COMMENT '좌석 물리 상태',
    PRIMARY KEY (seat_id),
    CONSTRAINT fk_seat_train FOREIGN KEY (train_id) REFERENCES train (train_id)
) COMMENT '좌석';

-- 열차 운행 (Train_Run)
CREATE TABLE IF NOT EXISTS train_run (
    train_run_id  INT         NOT NULL AUTO_INCREMENT COMMENT '운행 식별자',
    train_id      INT         NOT NULL COMMENT '열차 식별자',
    run_date      DATE        NOT NULL COMMENT '운행 날짜',
    run_status    VARCHAR(20) NOT NULL COMMENT '운행 상태',
    PRIMARY KEY (train_run_id),
    CONSTRAINT fk_train_run_train FOREIGN KEY (train_id) REFERENCES train (train_id)
) COMMENT '열차 운행';

-- 정차 정보 (Train_Stop)
CREATE TABLE IF NOT EXISTS train_stop (
    train_stop_id   INT  NOT NULL AUTO_INCREMENT COMMENT '정차정보 식별자',
    train_run_id    INT  NOT NULL COMMENT '운행 식별자',
    station_id      INT  NOT NULL COMMENT '역 식별자',
    stop_order      INT  NOT NULL COMMENT '정차 순서',
    arrival_time    TIME NULL     COMMENT '도착 시간',
    departure_time  TIME NULL     COMMENT '출발 시간',
    PRIMARY KEY (train_stop_id),
    CONSTRAINT fk_train_stop_run     FOREIGN KEY (train_run_id) REFERENCES train_run (train_run_id),
    CONSTRAINT fk_train_stop_station FOREIGN KEY (station_id)   REFERENCES station (station_id)
) COMMENT '정차 정보';

-- 운임 (Fare)
CREATE TABLE IF NOT EXISTS fare (
    fare_id               INT           NOT NULL AUTO_INCREMENT COMMENT '운임 식별자',
    departure_station_id  INT           NOT NULL COMMENT '출발역 식별자',
    arrival_station_id    INT           NOT NULL COMMENT '도착역 식별자',
    seat_class            VARCHAR(20)   NOT NULL COMMENT '좌석 등급',
    fare_amount           DECIMAL(10,2) NOT NULL COMMENT '기준 운임',
    PRIMARY KEY (fare_id),
    CONSTRAINT fk_fare_departure FOREIGN KEY (departure_station_id) REFERENCES station (station_id),
    CONSTRAINT fk_fare_arrival   FOREIGN KEY (arrival_station_id)   REFERENCES station (station_id)
) COMMENT '운임';

-- 탑승객 (Passenger)
CREATE TABLE IF NOT EXISTS passenger (
    passenger_id    INT         NOT NULL AUTO_INCREMENT COMMENT '승객 식별자',
    passenger_name  VARCHAR(50) NOT NULL COMMENT '승객 이름',
    birth_date      DATE        NULL     COMMENT '생년월일',
    PRIMARY KEY (passenger_id)
) COMMENT '탑승객';

-- 예약 (Reservation)
CREATE TABLE IF NOT EXISTS reservation (
    reservation_id      INT         NOT NULL AUTO_INCREMENT COMMENT '예약 식별자',
    member_id           INT         NOT NULL COMMENT '회원 식별자',
    reservation_code    VARCHAR(30) NOT NULL COMMENT '예약번호',
    reservation_date    DATETIME    NOT NULL COMMENT '예약생성일시',
    reservation_status  VARCHAR(20) NOT NULL COMMENT '예약상태',
    PRIMARY KEY (reservation_id),
    UNIQUE KEY uq_reservation_code (reservation_code),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member (member_id)
) COMMENT '예약';

-- 예약 상세 (Reservation_Detail)
CREATE TABLE IF NOT EXISTS reservation_detail (
    reservation_detail_id  INT           NOT NULL AUTO_INCREMENT COMMENT '예약상세 식별자',
    reservation_id         INT           NOT NULL COMMENT '예약 식별자',
    passenger_id           INT           NOT NULL COMMENT '승객 식별자',
    train_run_id           INT           NOT NULL COMMENT '운행 식별자',
    seat_id                INT           NOT NULL COMMENT '좌석 식별자',
    fare_id                INT           NOT NULL COMMENT '운임 식별자',
    departure_station_id   INT           NOT NULL COMMENT '탑승역 식별자',
    arrival_station_id     INT           NOT NULL COMMENT '하차역 식별자',
    fare_amount            DECIMAL(10,2) NOT NULL COMMENT '예약 확정 운임',
    detail_status          VARCHAR(20)   NOT NULL COMMENT '예약상세 상태',
    PRIMARY KEY (reservation_detail_id),
    CONSTRAINT fk_rd_reservation FOREIGN KEY (reservation_id)       REFERENCES reservation (reservation_id),
    CONSTRAINT fk_rd_passenger   FOREIGN KEY (passenger_id)         REFERENCES passenger (passenger_id),
    CONSTRAINT fk_rd_train_run   FOREIGN KEY (train_run_id)         REFERENCES train_run (train_run_id),
    CONSTRAINT fk_rd_seat        FOREIGN KEY (seat_id)              REFERENCES seat (seat_id),
    CONSTRAINT fk_rd_fare        FOREIGN KEY (fare_id)              REFERENCES fare (fare_id),
    CONSTRAINT fk_rd_departure   FOREIGN KEY (departure_station_id) REFERENCES station (station_id),
    CONSTRAINT fk_rd_arrival     FOREIGN KEY (arrival_station_id)   REFERENCES station (station_id)
) COMMENT '예약 상세';

-- 결제 (Payment)
CREATE TABLE IF NOT EXISTS payment (
    payment_id      INT           NOT NULL AUTO_INCREMENT COMMENT '결제 식별자',
    reservation_id  INT           NOT NULL COMMENT '예약 식별자',
    payment_amount  DECIMAL(10,2) NOT NULL COMMENT '결제금액',
    payment_method  VARCHAR(20)   NOT NULL COMMENT '결제수단',
    payment_status  VARCHAR(20)   NOT NULL COMMENT '결제상태',
    paid_at         DATETIME      NULL     COMMENT '결제완료일시',
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (reservation_id)
) COMMENT '결제';

-- 환불 (Refund)
CREATE TABLE IF NOT EXISTS refund (
    refund_id      INT           NOT NULL AUTO_INCREMENT COMMENT '환불 식별자',
    payment_id     INT           NOT NULL COMMENT '결제 식별자',
    refund_amount  DECIMAL(10,2) NOT NULL COMMENT '환불 금액',
    refund_status  VARCHAR(20)   NOT NULL COMMENT '환불 상태',
    requested_at   DATETIME      NOT NULL COMMENT '환불 요청일시',
    refunded_at    DATETIME      NULL     COMMENT '환불 완료일시',
    PRIMARY KEY (refund_id),
    CONSTRAINT fk_refund_payment FOREIGN KEY (payment_id) REFERENCES payment (payment_id)
) COMMENT '환불';

-- 승차권 (Ticket)
CREATE TABLE IF NOT EXISTS ticket (
    ticket_id              INT         NOT NULL AUTO_INCREMENT COMMENT '승차권 식별자',
    reservation_detail_id  INT         NOT NULL COMMENT '예약상세 식별자',
    issued_at              DATETIME    NULL     COMMENT '발권일시',
    ticket_status          VARCHAR(20) NOT NULL COMMENT '승차권 상태',
    PRIMARY KEY (ticket_id),
    UNIQUE KEY uq_ticket_reservation_detail (reservation_detail_id),
    CONSTRAINT fk_ticket_reservation_detail FOREIGN KEY (reservation_detail_id)
        REFERENCES reservation_detail (reservation_detail_id)
) COMMENT '승차권';