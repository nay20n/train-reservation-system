package com.train.reservation.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API명세.md 에 정의된 에러 코드 목록.
 * 명세에 코드를 추가하면 여기에도 같이 추가한다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
	SAME_STATION(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같습니다."),

	// 401
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

	// 404
	TRAIN_RUN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 운행입니다."),
	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),

	// 409
	DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
	SEAT_ALREADY_RESERVED(HttpStatus.CONFLICT, "이미 예약된 좌석입니다."),
	ALREADY_PAID(HttpStatus.CONFLICT, "이미 결제된 예약입니다."),
	RESERVATION_CANCELLED(HttpStatus.CONFLICT, "취소된 예약입니다."),
	ALREADY_CANCELLED(HttpStatus.CONFLICT, "이미 취소된 예약입니다.");

	private final HttpStatus status;
	private final String message;
}
