package min.taskflow.dashboard.dto;

import lombok.Builder;

@Builder
public record TaskDashboardStatsResponse(Long totalTasks,
                                         Long completedTasks,
                                         Long inProgressTasks,
                                         Long todoTasks,
                                         Long overdueTasks,
                                         Long teamProgress,
                                         Long myTasksToday,
                                         Long completionRate) {
}
