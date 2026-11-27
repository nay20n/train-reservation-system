package com.train.reservation.reservation;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * reservation 테이블 한 행 (예약).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Reservation {

	@Id
	private Integer reservationId;          // reservation_id: 예약 식별자
	private Integer memberId;               // member_id: 회원 식별자 (FK)
	private String reservationCode;         // reservation_code: 예약번호
	private LocalDateTime reservationDate;  // reservation_date: 예약생성일시
	private String reservationStatus;       // reservation_status: 예약상태
}
