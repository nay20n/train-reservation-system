# train-reservation-system 작업 규칙

## 기본
- 항상 한국어로 답한다.
- 사용자가 "읽기만 해", "코드 변경하지 마"라고 하면 파일을 수정하지 않고 설명과 수정 예시만 보여준다.

## Git: 허락 없이는 절대 진행하지 않기
아래 작업은 사용자가 **그때그때 명시적으로 허락했을 때만** 진행한다.
한 번 허락받았다고 다음에도 허락된 것으로 보지 않는다. (스킬 안에서 실행할 때도 마찬가지)

- commit
- pull / fetch 후 병합
- push
- merge / rebase / cherry-pick
- 브랜치 생성·전환·삭제 (checkout, switch, branch -d)
- reset, restore, stash, clean 등 작업 내용을 되돌리거나 지우는 명령
- git 설정 변경 (`git config`, `git update-index` 등)

스테이징(`git add`, `git mv`)도 요청이 있을 때만 한다. 파일 이동은 `git mv` 대신 일반 `mv`로 한다.
커밋 메시지는 추천만 하고, 실행은 사용자가 직접 하거나 허락한 뒤에 한다.
커밋 메시지 형식은 기존 이력을 따른다: `docs: ...`, `chore(back): ...`, `feat(back): ...`, `fix(back): ...`

## DB 데이터: 허락 없이는 절대 진행하지 않기
- INSERT / UPDATE / DELETE / DROP / TRUNCATE 등 데이터나 테이블을 바꾸는 SQL 실행
- `db/data.sql` 내용 추가·수정 (서버 시작 시 자동 실행되므로 데이터 변경과 같다)
- `spring.sql.init.*` 설정 변경
- 서버 실행(`./gradlew bootRun`)이나 DB에 붙는 테스트 실행 — `schema.sql`·`data.sql`이 자동 실행되기 때문

SELECT로 조회만 하는 것도 먼저 어떤 쿼리를 실행할지 알려준다.

## 개발 정책 (`docs/개발정책.md`)
- 회원·테스트·DB·브랜치 등 팀에서 정한 정책은 `docs/개발정책.md`를 따른다. 이 파일과 내용이 다르면 개발정책.md가 우선이다.
- 사용자와 새 정책을 정하면 개발정책.md에 추가할지 물어본다.

## API 명세 (`docs/API명세.md`)
- 이 파일은 **프론트엔드에 공유하는 문서**다. 구현은 명세를 그대로 따른다.
- 구현하다가 명세를 바꿔야 할 것 같으면 코드를 맞춰 바꾸지 말고, 먼저 사용자에게 무엇을 왜 바꿔야 하는지 말한다.
- 명세를 고치게 되면 프론트가 알아볼 수 있게 바뀐 부분을 정리해서 알려준다.
- 에러 코드는 명세 표와 `global/exception/ErrorCode.java`가 항상 일치해야 한다.

## 백엔드 구조 (Spring Data JDBC)
기술: Java 21, Spring Boot 4.1.2, Spring Data JDBC, MySQL 8.4, Lombok

도메인별 패키지 안에 계층을 둔다.

```
com.train.reservation
├── global/exception/        ← ErrorCode, BusinessException, ApiErrorResponse, GlobalExceptionHandler
├── member/                  ← 예: 회원
│   ├── Member.java              엔티티 (@Table, @Id)
│   ├── MemberRepository.java    Spring Data JDBC Repository
│   ├── MemberService.java       비즈니스 로직, @Transactional
│   ├── MemberController.java    @RestController, URL은 /api/...
│   └── dto/                     요청·응답 DTO (record)
├── station/
├── train/                   ← train, seat, train_run, train_stop, fare
├── reservation/             ← reservation, reservation_detail, passenger, ticket
└── payment/                 ← payment, refund
```

- **Controller**: 요청 받고 응답만 한다. 로직은 Service로. 엔티티를 그대로 응답하지 않고 DTO로 바꾼다.
- **Service**: 상태 변경은 `@Transactional`. 실패는 `throw new BusinessException(ErrorCode.XXX)`.
- **Repository**: `CrudRepository`/`ListCrudRepository` 상속. 여러 테이블 조인 조회는 `@Query`로 네이티브 SQL을 쓰고 결과는 DTO/record로 받는다.
- **엔티티**: Spring Data JDBC는 지연로딩이 없다. 다른 애그리거트는 객체 대신 ID(`Integer`, 필요하면 `AggregateReference`)로 참조한다.
- 컬럼명은 snake_case, 자바 필드는 camelCase (Spring Data JDBC가 자동 변환).
- 금액은 `BigDecimal` (DB `DECIMAL(10,2)`), 날짜 `LocalDate`, 시간 `LocalTime`, 일시 `LocalDateTime`.
- JSON 시간 형식은 명세대로 `06:00` (초 없음) — `@JsonFormat(pattern = "HH:mm")`.
- 상태값은 명세의 한글 값(`예약대기`, `결제완료` 등)을 그대로 DB에 저장한다.

## SQL 파일
- 모두 `src/main/resources/db/`에 둔다.
  - `schema.sql`: 테이블 생성 (`CREATE TABLE IF NOT EXISTS`). 서버 시작 시 자동 실행.
  - `data.sql`: 기준 데이터. 서버를 켤 때마다 실행되므로 **`INSERT IGNORE` + ID 직접 지정**으로 중복을 막는다.
- 테이블 구조를 바꾸면 `schema.sql`만 고쳐서는 반영되지 않는다(이미 있으면 건너뜀). 해당 테이블 DROP이 필요하므로 사용자에게 알린다.

## 작업 환경 차이 고려
- 맥 + 윈도우 팀원이 함께 쓴다. 모든 소스는 **UTF-8**. 윈도우 Eclipse/STS 기본값(MS949)에서는 한글이 깨질 수 있다.
- 줄바꿈: 기존 파일의 줄바꿈 방식을 유지해서 파일 전체가 바뀐 것처럼 보이지 않게 한다. (`gradlew`는 LF, `*.bat`은 CRLF — `.gitattributes`)
- STS/Eclipse 빌드 결과물 `bin/`, Gradle `build/`, IntelliJ `out/`은 git에 올리지 않는다. `bin/main/`에 예전 `schema.sql`이 남아 있을 수 있는데 무시한다.
- 맥 전용 `.DS_Store`는 올리지 않는다.

## 스킬
- `/api <번호 또는 기능명>`: API명세.md의 API 하나를 Entity → Repository → Service → Controller 순서로 구현한다. (`.claude/skills/api/SKILL.md`)
