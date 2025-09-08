package min.taskflow.team.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamErrorCode implements ErrorCode {

    //1XX 조회 관련 오류
    TEAM_NOT_FOUND("TEAM-101",HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    DUPLICATE_TEAM_NAME("TEAM-102", HttpStatus.CONFLICT, "이미 존재하는 팀 이름입니다."),
    MEMBER_NOT_FOUND("TEAM-103",HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),
    MEMBER_NOT_IN_TEAM("TEAM-104",HttpStatus.BAD_REQUEST, "해당 멤버는 이 팀에 속하지 않습니다."),
    MEMBER_ALREADY_IN_TEAM("TEAM-105", HttpStatus.BAD_REQUEST, "해당 멤버는 이미 다른 팀에 속해 있습니다."),

    //2XX 검색 관련 오류
    INVALID_USER_ID("TEAM-201", HttpStatus.BAD_REQUEST, "잘못된 사용자 ID 입니다."),
    INVALID_QUERY("TEAM-202", HttpStatus.BAD_REQUEST, "검색어는 비어있을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
