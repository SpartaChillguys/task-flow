package min.taskflow.log.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.log.Service.ActivityLogService;
import min.taskflow.log.response.ActivityLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<Page<ActivityLogResponse>>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ActivityLogResponse> data = activityLogService.getLogs(page, size);

        return
                ApiResponse.success(data, "활동 로그를 조회했습니다.");
    }
}
