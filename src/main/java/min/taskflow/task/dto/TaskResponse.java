package min.taskflow.task.dto;

import lombok.Builder;
import lombok.Getter;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Task;
import min.taskflow.task.entity.TaskStatus;
import min.taskflow.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private TaskStatus status;
    private Long assigneeId;
    private UserTaskResponse assignee;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    //Task를 TaskResponse로 매퍼 변환을 위한 메서드
    public static TaskResponse from(Task task) {

        User assignee = task.getAssignee();

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(TaskStatus.valueOf(task.getStatus().name()))
                .assigneeId(assignee != null ? assignee.getUserId() : null)
                .assignee(assignee == null ? null :
                        UserTaskResponse.builder()
                                .userId(assignee.getUserId())
                                .name(assignee.getName())
                                .email(assignee.getEmail())
                                .build()
                )
                .createAt(task.getCreatedAt())
                .updateAt(task.getUpdatedAt())
                .build();
    }


}
