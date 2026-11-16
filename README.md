# 🚄 기차 예약 시스템 (train-reservation-system)

## 1. 개발 환경

| 항목 | 버전 / 도구 |
| --- | --- |
| 백엔드 | Java 21, Spring Boot 4.1.2, Spring Data JDBC, Gradle |
| 프론트엔드 | Node.js 24 LTS, Vue 3 (JavaScript), Vite, Vue Router |
| DB | MySQL 8.4 LTS |
| DB 툴 | MySQL Workbench |
| 협업 | GitHub |

## 2. 프로젝트 구조

레포 하나에 백엔드와 프론트엔드가 같이 들어 있어요.

```
train-reservation-system/
├── build.gradle, gradlew          ← 백엔드 (Spring Boot)
├── src/main/java/...              ← 백엔드 코드
├── src/main/resources/
│   ├── application.properties     ← DB 연결 설정
│   ├── schema.sql                 ← 테이블 생성 DDL (서버 시작 시 자동 실행)
│   └── static/                    ← Vue 빌드 결과물이 들어가는 곳
└── frontend/                      ← 프론트엔드 (Vue)
```

- 개발할 때: 백엔드(8080 포트)와 프론트(5173 포트)를 따로 실행해요.
- 완성 후: Vue를 빌드하면 static 폴더로 들어가고, Spring 서버 하나로 화면과 API가 함께 실행돼요.

## 3. 공통 준비

### 3-1. MySQL 설치

1. dev.mysql.com/downloads/mysql 에서 **8.4 LTS** 설치
    - Mac: DMG (ARM 64-bit) / Windows: MSI Installer
2. 설치 중 root 비밀번호 설정 (각자 기억해두기)
3. MySQL 서버가 실행 중인지 확인
    - Mac: 시스템 설정 → MySQL → 초록불

### 3-2. MySQL Workbench 설치 및 계정 만들기

1. dev.mysql.com/downloads/workbench 에서 설치
2. Database Connections 옆 `+` 클릭 → User Name `root`, Host `localhost`, Port `3306`
→ Store Password에 root 비밀번호 입력 → Test Connection → OK
3. root로 접속해서 아래 SQL 실행 (**계정명과 비밀번호는 모두 똑같이**)
    
    CREATE DATABASE IF NOT EXISTS train_reservation DEFAULT CHARACTER SET utf8mb4;
    CREATE USER 'train'@'localhost' IDENTIFIED BY '1234';
    GRANT ALL PRIVILEGES ON train_reservation.* TO 'train'@'localhost';
    
4. (선택) `train` 계정으로 연결을 하나 더 만들어두기
    - Default Schema는 `train_reservation`

> 테이블은 직접 만들 필요 없어요. 백엔드 서버를 켜면 schema.sql이 자동으로 실행돼서 만들어져요.
> 

## 자주 나는 오류

| 오류 | 원인 / 해결 |
| --- | --- |
| Can't connect to MySQL server (61) | MySQL 서버가 꺼져 있음 → 시스템 설정에서 MySQL 켜기 |
| Access denied for user 'train' | train 계정이 없거나 비밀번호가 다름 → 3-2의 SQL 다시 확인 |
| Cannot find a Java installation ... 21 | JDK 21 미설치 → Temurin 21 설치 |
| 프론트에서 API 호출 시 404 / CORS | 백엔드 서버 실행 여부, vite proxy 설정 확인 |
