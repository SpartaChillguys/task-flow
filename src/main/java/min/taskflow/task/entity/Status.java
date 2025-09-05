package min.taskflow.task.entity;

// TODO: 순차적 변경에 대한 논의 후 로직 작성하기.
public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    public Status next() {
        return switch (this) {
            case TODO -> IN_PROGRESS;
            case IN_PROGRESS -> DONE;
            case DONE -> TODO;
        };
    }
}
