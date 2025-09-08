package min.taskflow.dashboard.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiPageResponse;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.dashboard.dto.RecentActivityResponse;
import min.taskflow.dashboard.dto.TaskDashboardStatsResponse;
import min.taskflow.dashboard.dto.TaskSummaryResponse;
import min.taskflow.dashboard.dto.TeamProgressResponse;
import min.taskflow.dashboard.service.ExternalQueryDashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ExternalQueryDashboardService externalQueryDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TaskDashboardStatsResponse>> getDashboardStats(@AuthenticationPrincipal Long LoginUserId) {

        TaskDashboardStatsResponse response = externalQueryDashboardService.getDashboardStats(LoginUserId);

        return ApiResponse.success(response, "대시보드 통계 조회 완료");
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<TaskSummaryResponse>> getTaskSummary(@AuthenticationPrincipal Long loginUserId) {

        TaskSummaryResponse response = externalQueryDashboardService.getTaskSummary(loginUserId);

        return ApiResponse.success(response, "내 작업 요약 조회 완료");
    }

    @GetMapping("/team-progress")
    public ResponseEntity<ApiResponse<TeamProgressResponse>> getTeamsProgress() {

        TeamProgressResponse response = externalQueryDashboardService.getTeamProgress();

        return ApiResponse.success(response, "팀 진행률 조회 완료");
    }

    @GetMapping("/activities")
    public ResponseEntity<ApiPageResponse<RecentActivityResponse>> getRecentActivities(@AuthenticationPrincipal Long loginUserId,
                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<RecentActivityResponse> response = externalQueryDashboardService.getRecentActivities(loginUserId, pageable);

        return ApiPageResponse.success(response, "활동 내역 조회 완료");
    }
}
