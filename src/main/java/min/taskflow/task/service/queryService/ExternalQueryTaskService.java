package min.taskflow.task.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.condition.TaskSearchCondition;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryTaskService {

    private final TaskMapper taskMapper;
    private final InternalQueryUserService internalQueryUserService;
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

        // taskId에 해당하는 task가 존재하는지 검증
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        // User Data 불러오기
        UserSearchAndAssigneeResponse assigneeResponse = internalQueryUserService.getAssigneeByUserId(task.getAssigneeId());

        // DTO 변환
        TaskResponse taskResponse = taskMapper.toTaskResponse(task, assigneeResponse);

        return taskResponse;
    }
}
