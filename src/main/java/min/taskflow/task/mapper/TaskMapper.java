package min.taskflow.task.mapper;

import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.response.dashboard.TaskDashboardStatsResponse;
import min.taskflow.task.dto.response.dashboard.TaskSummaryResponse;
import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest taskCreateRequest) {

        return Task.builder()
                .title(taskCreateRequest.title())
                .description(taskCreateRequest.description())
                .dueDate(taskCreateRequest.dueDate())
                .priority(taskCreateRequest.priority())
                .status(Status.TODO)
                .assigneeId(taskCreateRequest.assigneeId())
                .build();
    }

    public <T> TaskResponse<T> toTaskResponse(Task task, T assigneeInfoResponse) {

        return TaskResponse.<T>builder()
                .id(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(task.getAssigneeId())
                .assigneeInfoResponse(assigneeInfoResponse)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public TaskSummaryResponse toTaskSummaryResponse(List<Task> todayTasks,
                                                     List<Task> upcomingTasks,
                                                     List<Task> overdueTasks) {

        return TaskSummaryResponse.builder()
                .todayTasks(toTaskSummaryDtos(todayTasks))
                .upcomingTasks(toTaskSummaryDtos(upcomingTasks))
                .overdueTasks(toTaskSummaryDtos(overdueTasks))
                .build();
    }

    private List<TaskSummaryResponse.TaskSummaryDto> toTaskSummaryDtos(List<Task> tasks) {

        return tasks.stream()
                .map(task -> new TaskSummaryResponse.TaskSummaryDto(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getDueDate()
                ))
                .toList();
    }

    public TaskDashboardStatsResponse toTaskDashboardStatsResponse(Long totalTasks,
                                                                   Long completedTasks,
                                                                   Long inProgressTasks,
                                                                   Long todoTasks,
                                                                   Long overdueTasks,
                                                                   Long teamProgress,
                                                                   Long myTasksToday,
                                                                   Long completionRate) {

        return TaskDashboardStatsResponse.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .inProgressTasks(inProgressTasks)
                .todoTasks(todoTasks)
                .overdueTasks(overdueTasks)
                .teamProgress(teamProgress)
                .myTasksToday(myTasksToday)
                .completionRate(completionRate)
                .build();
    }
}
