package min.taskflow.task.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements ErrorCode {

    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "태스크를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
