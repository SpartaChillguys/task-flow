package min.taskflow.task.controllder;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.task.service.TaskDummyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskDummyService taskDummyService;

    // TODO: 204 응답 성공 예시
    public ResponseEntity<ApiResponse<Object>> a() {
        return ApiResponse.noContent("메시지 추가해야 됨");
    }

    @PostMapping("/task")
    public String createDummyTask(@RequestParam String userName) {
        taskDummyService.dummyTaskCreate(userName);
        return "Task created by " + userName;
    }
}
