package min.taskflow.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 1xx 검증 관련
    ALREADY_EXIST_EMAIL("USER-101", HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    ALREADY_EXIST_USERNAME("USER-102", HttpStatus.CONFLICT, "이미 존재하는 유저 이름입니다."),

    // 2xx 조회 관련
    USER_NOT_FOUND("USER-201", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
