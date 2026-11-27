package com.train.reservation.station;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * station 테이블 한 행 (역).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("station")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Station {

	@Id
	private Integer stationId;   // station_id: 역 식별자
	private String stationName;  // station_name: 역 이름
	private String city;         // city: 도시/지역
	private String address;      // address: 역 주소
}
