# naem-server
나음 BE 레포지토리입니다.

## Version
- Spring Boot 2.4.7
- Gradle: 7.4.1
- Java 11

## Getting Started
- local database setting
    - MySQL: 8.0.30
    - Redis: 7.0.0

<br/>

- 설정 파일 (`.gitignore` 등록)
    - `src/main/resources/application.yml`
    - `src/main/resources/firebase/firebase-service-key.json`

<br/>

- QueryDSL 빌드
    - `./gradlew clean compileQuerydsl`

<br/>

- 실행
    - `./gradlew bootRun`
