package min.taskflow.task.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.response.dashboard.TaskSummaryResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalQueryTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // TASK ID를 통해 TASK를 조회
    public Task getTaskByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        return task;
    }

    // TASK ID를 통해 존재 유무 확인
    public void validateTaskExists(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND);
        }
    }

    // DashBoard로 todayTasks, upcomingTasks, overdueTasks에 대한 리스트를 반환
    public TaskSummaryResponse getTaskSummaryByUserId(Long LoginUserId) {

        LocalDate baseDate = LocalDate.now();
        LocalDateTime startOfDay = baseDate.atStartOfDay();
        LocalDateTime endOfDay = baseDate.atTime(LocalTime.MAX);

        List<Task> todayTasks = taskRepository.findByAssigneeIdAndDueDateBetween(LoginUserId, startOfDay, endOfDay);
        List<Task> upcomingTasks = taskRepository.findByAssigneeIdAndDueDateAfter(LoginUserId, startOfDay.plusDays(1));
        List<Task> overdueTasks = taskRepository.findByAssigneeIdAndDueDateBefore(LoginUserId, startOfDay);

        TaskSummaryResponse taskSummaryResponse = taskMapper.toTaskSummaryResponse(todayTasks, upcomingTasks, overdueTasks);

        return taskSummaryResponse;
    }
}
