package com.train.reservation.reservation;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * reservation_detail 테이블 한 행 (예약 상세).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("reservation_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class ReservationDetail {

	@Id
	private Integer reservationDetailId;  // reservation_detail_id: 예약상세 식별자
	private Integer reservationId;        // reservation_id: 예약 식별자 (FK)
	private Integer passengerId;          // passenger_id: 승객 식별자 (FK)
	private Integer trainRunId;           // train_run_id: 운행 식별자 (FK)
	private Integer seatId;               // seat_id: 좌석 식별자 (FK)
	private Integer fareId;               // fare_id: 운임 식별자 (FK)
	private Integer departureStationId;   // departure_station_id: 탑승역 식별자 (FK)
	private Integer arrivalStationId;     // arrival_station_id: 하차역 식별자 (FK)
	private BigDecimal fareAmount;        // fare_amount: 예약 확정 운임
	private String detailStatus;          // detail_status: 예약상세 상태
}
