package min.taskflow.task.dto.response.dashboard;

import lombok.Builder;
import min.taskflow.task.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TaskSummaryResponse(List<TaskSummaryDto> todayTasks,
                                  List<TaskSummaryDto> upcomingTasks,
                                  List<TaskSummaryDto> overdueTasks
) {


    public record TaskSummaryDto(Long taskId,
                                 String title,
                                 Status status,
                                 LocalDateTime dueDate) {
    }
}
