package min.taskflow.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 1xx 토큰 관련
    INVALID_ACCESS_TOKEN("AUTH-001", HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN("AUTH-002", HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // 2xx 로그인 자격 증명 관련
    WRONG_PASSWORD("AUTH-201", HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    WRONG_USERNAME("AUTH-202", HttpStatus.UNAUTHORIZED, "잘못된 사용자명입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
