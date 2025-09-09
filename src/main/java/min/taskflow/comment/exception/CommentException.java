package min.taskflow.comment.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;


public class CommentException extends GlobalException {
    public CommentException(ErrorCode errorCode){
        super(errorCode);
    }
}
