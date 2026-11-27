package com.train.reservation.reservation;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * passenger 테이블 한 행 (탑승객).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("passenger")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Passenger {

	@Id
	private Integer passengerId;   // passenger_id: 승객 식별자
	private String passengerName;  // passenger_name: 승객 이름
	private LocalDate birthDate;   // birth_date: 생년월일
}
