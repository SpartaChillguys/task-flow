package min.taskflow.task.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements ErrorCode {

    // TODO: 에러의 종류별로 묶어서 관리할 수 있음.
    TASK_NOT_FOUND("TASK-101", HttpStatus.NOT_FOUND, "태스크를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
