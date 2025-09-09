package min.taskflow.auth.exception;

import min.taskflow.common.exception.ErrorCode;
import min.taskflow.common.exception.GlobalException;


public class AuthException extends GlobalException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
