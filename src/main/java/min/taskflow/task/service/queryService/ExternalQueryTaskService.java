package min.taskflow.task.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.taskRepository.TaskRepository;
import min.taskflow.user.dto.response.AssigneeResponse;
import min.taskflow.user.service.InternalUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryTaskService {

    private final TaskMapper taskMapper;
    private final InternalUserService internalUserService;
    private final TaskRepository taskRepository;

    //TODO: 리팩토링 필요
    public List<TaskResponse> getTasksByTaskId(String keyWord,
                                               Pageable pageable
    ) {

        List<Task> taskList = taskRepository.findByKeyword(keyWord);

        List<TaskResponse> result = taskList.stream()
                .map(TaskResponse::from)
                .toList();

        return result;
    }

    public TaskResponse getTaskByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        AssigneeResponse assigneeResponse = internalUserService.getAssigneeByUserId(task.getAssigneeId());

        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }
}
