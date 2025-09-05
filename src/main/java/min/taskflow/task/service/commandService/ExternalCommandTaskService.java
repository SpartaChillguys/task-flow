package min.taskflow.task.service.commandService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.request.StatusUpdateRequest;
import min.taskflow.task.dto.request.TaskCreateRequest;
import min.taskflow.task.dto.request.TaskUpdateRequest;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalCommandTaskService {

    private final TaskMapper taskMapper;
    private final InternalUserService internalUserService;
    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskCreateRequest request) {

        Task task = taskMapper.toEntity(request);

        AssigneeResponse assigneeResponse = internalUserService.getAssigneeByUserId(request.getAssigneeId());

        TaskResponse taskResponse = taskMapper.toTaskResponse(taskRepository.save(task), assigneeResponse);

        return taskResponse;
    }

    public TaskResponse updateTaskDetailByTaskId(Long taskId, TaskUpdateRequest taskUpdateRequest) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        task.updateDetail(taskUpdateRequest);

        AssigneeResponse assigneeResponse = internalUserService.getAssigneeByUserId(task.getAssigneeId());

        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    public TaskResponse updateStatusByTaskId(Long taskId, StatusUpdateRequest statusUpdateRequest) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        //TODO: 순차적 변경 논의 후 로직 작성 후 적용하기
        task.getStatus().next();

        AssigneeResponse assigneeResponse = internalUserService.getAssigneeByUserId(task.getAssigneeId());

        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }

    public void deleteTaskByTaskId(Long taskId) {

        taskRepository.deleteById(taskId);
    }

}
