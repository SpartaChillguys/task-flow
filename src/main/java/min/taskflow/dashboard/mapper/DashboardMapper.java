package min.taskflow.dashboard.mapper;

import min.taskflow.dashboard.dto.RecentActivityResponse;
import min.taskflow.dashboard.dto.TargetDomainType;
import min.taskflow.dashboard.dto.TeamProgressResponse;
import min.taskflow.dashboard.dto.WeeklyTrendResponse;
import min.taskflow.log.ActivityType;
import min.taskflow.log.entity.Log;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class DashboardMapper {

    public TeamProgressResponse toTeamProgressResponse(Map<String, Long> teamsProgress) {

        return TeamProgressResponse.builder()
                .teamProgress(teamsProgress)
                .build();
    }

    private TargetDomainType resolveTargetType(ActivityType type) {
        return switch (type) {
            case TASK_CREATED, TASK_UPDATED, TASK_DELETED, TASK_STATUS_CHANGED -> TargetDomainType.TASK;
            case COMMENT_CREATED, COMMENT_UPDATED, COMMENT_DELETED -> TargetDomainType.COMMENT;
            case USER_LOGIN, USER_LOGOUT -> TargetDomainType.USER;
        };
    }

    public RecentActivityResponse toRecentActivityResponse(Log log, Long loginUserId, AssigneeSummaryResponse loginUser) {

        return RecentActivityResponse.builder()
                .id(log.getLogId())
                .userId(loginUserId)
                .user(loginUser)
                .action(log.getType())
                .targetType(resolveTargetType(log.getType()))
                .targetId(log.getTaskId())
                .description(log.getDescription())
                .createdAt(log.getTimeStamp())
                .build();
    }

    public WeeklyTrendResponse toWeeklyTrendResponse(String name, Long tasks, Long completed, LocalDate date) {

        return WeeklyTrendResponse.builder()
                .name(name)
                .tasks(tasks)
                .completed(completed)
                .date(date)
                .build();
    }
}
