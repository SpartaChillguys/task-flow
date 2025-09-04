package min.taskflow.task.service;

import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

import static min.taskflow.task.exception.TaskErrorCode.TASK_NOT_FOUND;

@Service
public class InternalTaskService {
    private final TaskRepository taskRepository;

    public InternalTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task findByTaskId(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskException(TASK_NOT_FOUND));
    }
}
