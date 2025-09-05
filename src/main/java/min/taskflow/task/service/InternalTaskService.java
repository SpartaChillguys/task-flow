package min.taskflow.task.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalTaskService {

    private final TaskRepository taskRepository;

    public Task findByTaskId(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
