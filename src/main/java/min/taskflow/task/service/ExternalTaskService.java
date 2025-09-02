package min.taskflow.task.service;

import min.taskflow.task.exception.InvalidException;
import min.taskflow.task.exception.TaskErrorCode;

public class ExternalTaskService {

    // TODO: 공통 예외 처리 사용 예시
    public void a() {
        throw new InvalidException(TaskErrorCode.TASK_NOT_FOUND);
    }

}
