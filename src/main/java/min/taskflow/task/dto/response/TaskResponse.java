package min.taskflow.task.dto.response;

import lombok.Builder;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;
import min.taskflow.user.dto.response.AssigneeResponse;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(Long id,
                           String title,
                           String description,
                           LocalDateTime dueDate,
                           Priority priority,
                           Status status,
                           Long assigneeId,
                           AssigneeResponse assigneeResponse,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
}