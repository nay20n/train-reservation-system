---
name: api
description: docs/API명세.md 에 있는 API 하나를 Spring Data JDBC 계층(Entity → Repository → Service → RestController)으로 구현한다. "회원가입 API 만들어줘", "/api 6", "예약하기 구현" 처럼 명세의 API를 구현해달라고 할 때 사용.
argument-hint: <API 번호 또는 기능명, 예: 1 / 회원가입>
---

# API 구현 스킬

인자: `$ARGUMENTS` (API명세.md 목록의 번호 또는 기능명)

프로젝트 루트의 `CLAUDE.md` 규칙을 그대로 따른다. 특히 **git 작업과 DB 데이터 변경은 매번 허락을 받는다.**

## 1. 확인 (코드 쓰기 전)
1. `docs/API명세.md`에서 해당 API의 URL, Method, 요청, 응답, 에러 표를 읽는다.
2. `src/main/resources/db/schema.sql`에서 관련 테이블과 컬럼을 확인한다.
3. 이미 있는 코드(같은 도메인 패키지, `global/exception`)를 확인해서 중복으로 만들지 않는다.
4. 사용자에게 짧게 정리해서 보여준다:
   - 만들/고칠 파일 목록
   - 사용할 테이블
   - 명세와 스키마가 안 맞는 부분이 있으면 (예: 응답에 필요한 값이 테이블에 없음) **여기서 멈추고 물어본다.** 명세를 임의로 바꾸지 않는다.

## 2. 구현 순서
도메인 패키지 `com.train.reservation.<도메인>` 아래에 만든다.

1. **엔티티** — `@Table("테이블명")`, `@Id`. Lombok `@Getter`, 생성은 정적 팩토리나 `@Builder`. 다른 애그리거트는 ID로 참조.
2. **Repository** — `ListCrudRepository<Entity, Integer>` 상속. 조인 조회는 `@Query` + 결과 record.
3. **DTO** — `dto/` 패키지에 record로. 요청 DTO에는 `@NotBlank`, `@NotNull`, `@NotEmpty` 등 검증. JSON 필드명은 명세와 **글자 하나까지 똑같이**.
4. **Service** — `@Service`, `@RequiredArgsConstructor`, 쓰기 메서드는 `@Transactional`. 명세의 에러는 `BusinessException(ErrorCode.XXX)`로 던진다. ErrorCode에 없으면 추가한다.
5. **Controller** — `@RestController`, `@RequestMapping("/api/...")`, 요청 바디는 `@Valid @RequestBody`. 상태코드는 명세대로 (`201`이면 `ResponseEntity.status(HttpStatus.CREATED)`).

## 3. 검증
- `./gradlew compileJava` 로 컴파일만 확인한다. (DB가 필요 없음)
- 서버 실행이나 DB에 붙는 테스트는 schema.sql/data.sql이 실행되므로 **허락받고** 한다.
- 기준 데이터가 필요하면 `db/data.sql`에 넣을 INSERT 문을 **제안만** 하고, 허락받은 뒤 추가한다.

## 4. 마무리 보고
- 만든 파일 목록과 각 파일 역할 한 줄씩
- 명세 대비 확인 결과 (요청/응답 필드, 상태코드, 에러코드)
- 프론트에 알려야 할 것이 있으면 따로 표시
- 추천 커밋 메시지 (예: `feat(back): 회원가입 API 구현`) — **커밋은 하지 않는다.** 사용자가 허락하면 그때 한다.
