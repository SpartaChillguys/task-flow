package min.taskflow.log.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.log.ActivityType;
import min.taskflow.log.Service.ActivityLogService;
import min.taskflow.log.response.ActivityLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

//    @GetMapping("/activities")
//    public ResponseEntity<ApiResponse<Page<ActivityLogResponse>>> getLogs(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Page<ActivityLogResponse> data = activityLogService.getLogs(page, size);
//
//        return
//                ApiResponse.success(data, "활동 로그를 조회했습니다.");
//    }

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<Page<ActivityLogResponse>>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ActivityType type,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Page<ActivityLogResponse> data =
                activityLogService.getLogs(page, size, type, userId, taskId, startDate, endDate);

        return
                ApiResponse.success(data, "활동 로그를 조회했습니다.");

    }
}
