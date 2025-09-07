package min.taskflow.task.dto.response.task;

import lombok.Builder;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;

import java.time.LocalDateTime;

@Builder
public record TaskResponse<T>(Long id,
                              String title,
                              String description,
                              LocalDateTime dueDate,
                              Priority priority,
                              Status status,
                              Long assigneeId,
                              T assigneeInfoResponse,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
}