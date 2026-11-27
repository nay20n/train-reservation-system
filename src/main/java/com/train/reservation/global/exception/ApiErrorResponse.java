package com.train.reservation.global.exception;

/**
 * 명세의 공통 에러 응답 형식: { "code": "...", "message": "..." }
 * (스프링의 org.springframework.web.ErrorResponse 와 이름이 겹치지 않게 ApiErrorResponse 로 지음)
 */
public record ApiErrorResponse(String code, String message) {

	public static ApiErrorResponse of(ErrorCode errorCode) {
		return new ApiErrorResponse(errorCode.name(), errorCode.getMessage());
	}
}
