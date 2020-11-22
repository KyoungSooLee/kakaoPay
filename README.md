# kakaoPay

## 1. 개발환경
  - eclipse
  - java8
  - mariadb
  - tomcat7
  - maven

## 2. 요구 사항
  기본 환경
IDE: IntelliJ IDEA Ultimate
OS: Mac OS X
GIT
Server
Java8
Spring Boot 2.2.2
JPA
H2
Gradle
Junit5

  ● 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
    - 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
    - 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
    - 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
  ● 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
  ● 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.
