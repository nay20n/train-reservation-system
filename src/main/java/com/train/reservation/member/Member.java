package com.train.reservation.member;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * member 테이블 한 행.
 * 컬럼 이름(snake_case)과 필드 이름(camelCase)은 Spring Data JDBC가 자동으로 연결한다.
 */
@Getter
@Table("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Spring Data JDBC가 DB에서 읽어올 때 사용
public class Member {

	/** 가입 시 회원상태 기본값 */
	public static final String STATUS_ACTIVE = "ACTIVE";

	@Id
	private Integer memberId;      // member_id (AUTO_INCREMENT → 저장 전에는 null)
	private String loginId;        // login_id
	private String password;       // password
	private String name;           // name
	private String phone;          // phone (선택)
	private String memberStatus;   // member_status ACTIVE/WITHDRAWN

	/**
	 * 회원가입용 생성 메서드.
	 * memberId는 비워두면 save() 할 때 DB가 번호를 붙여준다.
	 */
	public static Member create(String loginId, String password, String name, String phone) {
		Member member = new Member();
		member.loginId = loginId;
		member.password = password;
		member.name = name;
		member.phone = phone;
		member.memberStatus = STATUS_ACTIVE;
		return member;
	}
}
