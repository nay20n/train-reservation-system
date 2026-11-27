package com.train.reservation.train;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * fare 테이블 한 행 (운임).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("fare")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Fare {

	@Id
	private Integer fareId;              // fare_id: 운임 식별자
	private Integer departureStationId;  // departure_station_id: 출발역 식별자 (FK)
	private Integer arrivalStationId;    // arrival_station_id: 도착역 식별자 (FK)
	private String seatClass;            // seat_class: 좌석 등급
	private BigDecimal fareAmount;       // fare_amount: 기준 운임
}
