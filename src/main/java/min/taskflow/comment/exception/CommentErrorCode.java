package min.taskflow.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import min.taskflow.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    // 조회 관련 오류 1xx
    COMMENT_NOT_FOUND("CMT-101", HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    PARENT_COMMENT_NOT_FOUND("CMT-102", HttpStatus.NOT_FOUND, "부모 댓글을 찾을 수 없습니다."),

    // 권한/인증 오류 2xx
    COMMENT_FORBIDDEN("CMT-201", HttpStatus.FORBIDDEN, "댓글에 대한 권한이 없습니다."),

    // 상태/무결성 오류 3xx
    COMMENT_TASK_MISMATCH("CMT-301", HttpStatus.BAD_REQUEST, "댓글이 해당 작업에 속하지 않습니다."),
    COMMENT_ALREADY_DELETED("CMT-302", HttpStatus.BAD_REQUEST, "이미 삭제된 댓글입니다.");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
