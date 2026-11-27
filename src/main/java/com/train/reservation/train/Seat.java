package com.train.reservation.train;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * seat 테이블 한 행 (좌석).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("seat")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Seat {

	@Id
	private Integer seatId;     // seat_id: 좌석 식별자
	private Integer trainId;    // train_id: 열차 식별자 (FK)
	private Integer carNo;      // car_no: 호차 번호
	private String seatNo;      // seat_no: 좌석 번호
	private String seatClass;   // seat_class: 좌석 등급 STANDARD/FIRST
	private String seatStatus;  // seat_status: 좌석 물리 상태
}
