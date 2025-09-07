package min.taskflow.task.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.task.dto.response.dashboard.TaskDashboardStatsResponse;
import min.taskflow.task.dto.response.dashboard.TaskSummaryResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.team.service.query.InternalQueryTeamService;
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
    private final InternalQueryTeamService internalQueryTeamService;

    // TASK Id를 통해 TASK를 조회
    public Task getTaskByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        return task;
    }

    // DASHBOARD todayTasks, upcomingTasks, overdueTasks에 대한 리스트를 반환
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

    // DASHBOARD 통계 반환
    public TaskDashboardStatsResponse getDashboardStatsByUserId(Long LoginUserId) {

        LocalDate baseDate = LocalDate.now();
        LocalDateTime startOfDay = baseDate.atStartOfDay();
        LocalDateTime endOfDay = baseDate.atTime(LocalTime.MAX);
        List<Long> userIds = internalQueryTeamService.getMembersIdByUserId(LoginUserId);

        Long completedTasks = taskRepository.countByAssigneeIdInAndStatus(userIds, Status.DONE);
        Long inProgressTasks = taskRepository.countByAssigneeIdInAndStatus(userIds, Status.IN_PROGRESS);
        Long todoTasks = taskRepository.countByAssigneeIdInAndStatus(userIds, Status.TODO);
        Long totalTasks = completedTasks + inProgressTasks + todoTasks;
        Long overdueTasks = taskRepository.countByAssigneeIdAndDueDateBefore(LoginUserId, startOfDay);
        Long teamProgress = ((completedTasks + inProgressTasks) / totalTasks) * 100;
        Long myTasksToday = taskRepository.countByAssigneeIdAndDueDateBetween(LoginUserId, startOfDay, endOfDay);
        Long completionRate = (completedTasks / totalTasks * 100);

        TaskDashboardStatsResponse taskDashboardStatsResponse = taskMapper.toTaskDashboardStatsResponse(totalTasks,
                completedTasks,
                inProgressTasks,
                todoTasks,
                overdueTasks,
                teamProgress,
                myTasksToday,
                completionRate);

        return taskDashboardStatsResponse;
    }
}
