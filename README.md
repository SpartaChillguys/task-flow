# Taskflow
Taskflow는 기업 내 협업과 업무 관리를 위한 백엔드 중심의 태스크 관리 시스템입니다.  
도메인 주도 설계(DDD)와 명령-조회 책임 분리(CQRS) 아키텍처를 기반으로, 복잡한 업무 흐름을 명확하게 분리하고 유지보수성과 확장성을 극대화합니다.  
안정적인 트랜잭션 처리와 역할 기반 권한 제어를 통해 실시간 협업 환경에서도 신뢰성 있는 운영을 보장합니다.   
| [**API**](https://teamsparta.notion.site/S-A-Starting-Assignments-25a2dc3ef51481889299e106dea470d3?pvs=73)
| [**ERD**](https://www.erdcloud.com/d/xfrdtjSasQapxBzfi)
| [**DEMO**]유튜브링크

## Features

- 태스크 생성, 수정, 삭제 및 상태 관리
- 사용자 및 팀 기반 권한 설정
- 댓글 및 대댓글 기능 (수정/삭제 포함)
- 활동 로그 자동 기록 (댓글 생성/수정/삭제 등)
- CQRS 기반 서비스 분리: Command / Query 구조
- DDD 기반 도메인 설계: Task, Comment, User, Log 등 명확한 경계

## Tech Stack

| 계층       | 기술             |
|------------|------------------|
| Language   | Java 17          |
| Framework  | Spring Boot      |
| ORM        | JPA (Hibernate)  |
| Database   | MySQL            |
| Architecture | DDD, CQRS      |
| Deployment | Docker           |
| Testing    | JUnit 5, Mockito |

## Project Structure


