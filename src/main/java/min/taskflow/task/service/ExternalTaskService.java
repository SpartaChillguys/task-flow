package min.taskflow.task.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import min.taskflow.task.dto.TaskRequest;
import min.taskflow.task.dto.TaskResponse;
import min.taskflow.task.dto.UserTaskResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.task.entity.TaskStatus;
import min.taskflow.task.taskRepository.TaskRepository;
import min.taskflow.user.entity.User;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;


@Service
@NoArgsConstructor(force = true)
public class ExternalTaskService {

    private final TaskRepository taskRepository;

    public Task createTask(TaskRequest newTask) {

        Task task = new Task(
                newTask.getAssignee(),
                newTask.getDescription(),
                newTask.getDueDate(),
                newTask.getPriority(),
                newTask.getStatus(),
                newTask.getTitle()
        );

        taskRepository.save(task);

        return task;
    }

    public List<TaskResponse> getTaskListByTaskId(String keyWord,
                                                  Pageable pageable
    ) {

        List<Task> taskList = taskRepository.findByKeyword(keyWord);

        List<TaskResponse> result = taskList.stream()
                .map(TaskResponse::from)
                .toList();

        return result;
    }

    public TaskResponse getTask(Long taskId, String keyWord) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
    }


    public TaskResponse updateTaskById(Long taskId, TaskRequest updateRequest) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        User assignee = task.getAssignee();

        TaskResponse buildTask = TaskResponse.builder()
                .id(task.getId())
                .title(updateRequest.getTitle())
                .description(updateRequest.getDescription())
                .dueDate(updateRequest.getDueDate())
                .priority(updateRequest.getPriority())
                .status(TaskStatus.valueOf(updateRequest.getStatus().name()))
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

        return buildTask;
    }

    public void deleteTaskById(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        task.delete();
    }


    // TODO: 공통 예외 처리 사용 예시
//    public void a() {
//        throw new InvalidException(TaskErrorCode.TASK_NOT_FOUND);
//    }

}
