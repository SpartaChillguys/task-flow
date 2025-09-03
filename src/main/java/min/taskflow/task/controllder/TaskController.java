package min.taskflow.task.controllder;

import min.taskflow.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class TaskController {

    // TODO: 204 응답 성공 예시
    public ResponseEntity<ApiResponse<Object>> a() {
        return ApiResponse.noContent("메시지 추가해야 됨");
    }

}
