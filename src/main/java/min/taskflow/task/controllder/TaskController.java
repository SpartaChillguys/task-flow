package min.taskflow.task.controllder;

import lombok.NoArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.task.dto.TaskRequest;
import min.taskflow.task.dto.TaskResponse;
import min.taskflow.task.dto.UserTaskResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.task.entity.TaskStatus;
import min.taskflow.task.service.ExternalTaskService;
import min.taskflow.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@NoArgsConstructor(force = true)
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final ExternalTaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @RequestBody TaskRequest newTask) {

        Task task = taskService.createTask(newTask);

        User assignee = task.getAssignee();

        TaskResponse buildTask = TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(TaskStatus.valueOf(task.getStatus().name()))
                .assigneeId(assignee != null ? assignee.getUserId() : null)
                .assignee(assignee == null ? null :
                        UserTaskResponse.builder()
                                .userId(assignee.getUserId())
                                .name(assignee.getName())
                                .email(assignee.getEmail())
                                .build()
                )
                .createAt(task.getCreatedAt())
                .updateAt(task.getUpdatedAt())
                .build();

        return ApiResponse.success(buildTask, "Task를 생성하였습니다.");
    }

    @GetMapping("/viewList")
    public ResponseEntity<List<TaskResponse>> viewList(
            @RequestParam String keyWord,
            Pageable pageable
    ) {

        List<TaskResponse> tasks = taskService.getTaskListByTaskId(keyWord, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/viewSingle/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> viewSingle(@PathVariable Long taskId,
                                                                @RequestParam String keyWord
    ) {
        TaskResponse task = taskService.getTask(taskId, keyWord);

        return ApiResponse.success(task, "단건 조회를 성공하였습니다.");
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long taskId,
                                                                TaskRequest updateRequest
    ) {
        TaskResponse taskResponse = taskService.updateTaskById(taskId, updateRequest);

        return ApiResponse.success(taskResponse, "업데이트가 완료되었습니다");
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);

        return ResponseEntity.noContent().build();
    }


    // TODO: 204 응답 성공 예시
    public ResponseEntity<ApiResponse<Object>> a() {
        return ApiResponse.noContent("메시지 추가해야 됨");
    }

}
