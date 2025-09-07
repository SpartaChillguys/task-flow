package min.taskflow.task.entity;

import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;

public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    public Status next(Status target) {
        return switch (this) {
            case TODO -> {
                if (target == IN_PROGRESS) yield IN_PROGRESS;
                throw new TaskException(TaskErrorCode.INVALID_STATUS_UPDATE);
            }
            case IN_PROGRESS -> {
                if (target == DONE) yield DONE;
                throw new TaskException(TaskErrorCode.INVALID_STATUS_UPDATE);
            }
            case DONE -> throw new TaskException(TaskErrorCode.INVALID_STATUS_UPDATE);
        };
    }
}

