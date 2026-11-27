package com.train.reservation.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * payment 테이블 한 행 (결제).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Payment {

	@Id
	private Integer paymentId;         // payment_id: 결제 식별자
	private Integer reservationId;     // reservation_id: 예약 식별자 (FK)
	private BigDecimal paymentAmount;  // payment_amount: 결제금액
	private String paymentMethod;      // payment_method: 결제수단
	private String paymentStatus;      // payment_status: 결제상태
	private LocalDateTime paidAt;      // paid_at: 결제완료일시
}
