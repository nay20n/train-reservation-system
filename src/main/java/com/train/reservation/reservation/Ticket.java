package com.train.reservation.reservation;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ticket 테이블 한 행 (승차권).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("ticket")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Ticket {

	@Id
	private Integer ticketId;             // ticket_id: 승차권 식별자
	private Integer reservationDetailId;  // reservation_detail_id: 예약상세 식별자 (FK)
	private LocalDateTime issuedAt;       // issued_at: 발권일시
	private String ticketStatus;          // ticket_status: 승차권 상태
}
