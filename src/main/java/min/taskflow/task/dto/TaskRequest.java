package min.taskflow.task.dto;

import lombok.Getter;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.TaskStatus;
import min.taskflow.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class TaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime dueDate;
    private User assignee;

}
