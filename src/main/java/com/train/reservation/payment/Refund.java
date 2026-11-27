package com.train.reservation.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * refund 테이블 한 행 (환불).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("refund")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Refund {

	@Id
	private Integer refundId;           // refund_id: 환불 식별자
	private Integer paymentId;          // payment_id: 결제 식별자 (FK)
	private BigDecimal refundAmount;    // refund_amount: 환불 금액
	private String refundStatus;        // refund_status: 환불 상태
	private LocalDateTime requestedAt;  // requested_at: 환불 요청일시
	private LocalDateTime refundedAt;   // refunded_at: 환불 완료일시
}
