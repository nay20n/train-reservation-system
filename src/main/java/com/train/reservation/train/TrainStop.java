package com.train.reservation.train;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * train_stop 테이블 한 행 (정차 정보).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("train_stop")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class TrainStop {

	@Id
	private Integer trainStopId;      // train_stop_id: 정차정보 식별자
	private Integer trainRunId;       // train_run_id: 운행 식별자 (FK)
	private Integer stationId;        // station_id: 역 식별자 (FK)
	private Integer stopOrder;        // stop_order: 정차 순서
	private LocalTime arrivalTime;    // arrival_time: 도착 시간
	private LocalTime departureTime;  // departure_time: 출발 시간
}
