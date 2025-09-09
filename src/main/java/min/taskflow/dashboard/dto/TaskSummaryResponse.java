package min.taskflow.dashboard.dto;

import lombok.Builder;
import min.taskflow.task.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TaskSummaryResponse(List<TaskSummaryDto> todayTasks,
                                  List<TaskSummaryDto> upcomingTasks,
                                  List<TaskSummaryDto> overdueTasks
) {

    public record TaskSummaryDto(Long id,
                                 String title,
                                 Status status,
                                 LocalDateTime dueDate) {
    }
}
