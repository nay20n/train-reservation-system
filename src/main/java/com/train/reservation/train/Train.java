package com.train.reservation.train;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * train 테이블 한 행 (기차).
 * 다른 테이블은 객체 대신 ID(FK)로 참조한다.
 */
@Getter
@Table("train")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Train {

	@Id
	private Integer trainId;   // train_id: 열차 식별자
	private String trainNo;    // train_no: 열차번호
	private String trainType;  // train_type: 열차종류  KTX/ITX 
}
