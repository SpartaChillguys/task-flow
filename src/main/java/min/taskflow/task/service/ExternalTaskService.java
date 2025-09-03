package min.taskflow.task.service;

import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;

public class ExternalTaskService {

    // TODO: 공통 예외 처리 사용 예시
    public void a() {
        throw new TaskException(TaskErrorCode.TASK_NOT_FOUND);
    }
}
