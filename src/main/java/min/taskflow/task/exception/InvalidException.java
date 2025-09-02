package min.taskflow.task.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;


public class InvalidException extends GlobalException {
    public InvalidException(ErrorCode errorCode){
        super(errorCode);
    }
}
