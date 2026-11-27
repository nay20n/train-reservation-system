package com.train.reservation.global.exception;

import lombok.Getter;

/**
 * 서비스에서 명세에 정의된 실패 상황을 알릴 때 던진다.
 * 예) throw new BusinessException(ErrorCode.SEAT_ALREADY_RESERVED);
 */
@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
