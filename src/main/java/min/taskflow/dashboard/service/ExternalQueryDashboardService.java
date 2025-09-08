package min.taskflow.dashboard.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.dashboard.dto.RecentActivityResponse;
import min.taskflow.dashboard.dto.TaskDashboardStatsResponse;
import min.taskflow.dashboard.dto.TaskSummaryResponse;
import min.taskflow.dashboard.dto.TeamProgressResponse;
import min.taskflow.dashboard.mapper.DashboardMapper;
import min.taskflow.log.Service.ActivityLogService;
import min.taskflow.task.service.query.InternalQueryTaskService;
import min.taskflow.team.service.query.InternalQueryTeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalQueryDashboardService {

    private final DashboardMapper dashboardMapper;
    private final InternalQueryTaskService internalQueryTaskService;
    private final InternalQueryTeamService internalQueryTeamService;
    private final ActivityLogService activityLogService;

    public TaskDashboardStatsResponse getDashboardStats(Long LoginUserId) {

        TaskDashboardStatsResponse response = internalQueryTaskService.getDashboardStatsByUserId(LoginUserId);

        return response;
    }

    public TaskSummaryResponse getTaskSummary(Long loginUserId) {

        TaskSummaryResponse response = internalQueryTaskService.getTaskSummaryByUserId(loginUserId);

        return response;
    }

    public TeamProgressResponse getTeamProgress() {

        Map<String, List<Long>> teamUsersMap = internalQueryTeamService.getTeamsProgress();

        Map<String, Long> teamProgressMap = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : teamUsersMap.entrySet()) {
            String teamName = entry.getKey();
            List<Long> memberIds = entry.getValue();

            Long progress = internalQueryTaskService.getTeamsProgress(memberIds);
            teamProgressMap.put(teamName, progress);
        }

        TeamProgressResponse response = dashboardMapper.toTeamProgressResponse(teamProgressMap);

        return response;
    }

    public Page<RecentActivityResponse> getRecentActivities(Long loginUserId, Pageable pageable) {

        Page<RecentActivityResponse> response = activityLogService.getRecentActivities(loginUserId, pageable);

        return response;
    }
}
