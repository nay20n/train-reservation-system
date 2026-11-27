package com.train.reservation.train;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * train_run 테이블 한 행 (열차 운행).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("train_run")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class TrainRun {

	@Id
	private Integer trainRunId;  // train_run_id: 운행 식별자
	private Integer trainId;     // train_id: 열차 식별자 (FK)
	private LocalDate runDate;   // run_date: 운행 날짜
	private String runStatus;    // run_status: 운행 상태 SCHEDULED/RUNNING/COMPLETED/CANCELED 
}
