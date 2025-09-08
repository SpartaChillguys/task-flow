package min.taskflow.task.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.dashboard.dto.TaskDashboardStatsResponse;
import min.taskflow.dashboard.dto.TaskSummaryResponse;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.exception.TaskErrorCode;
import min.taskflow.task.exception.TaskException;
import min.taskflow.task.mapper.TaskMapper;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.team.service.query.InternalQueryTeamService;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;
import min.taskflow.user.service.query.InternalQueryUserService;
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
    private final InternalQueryUserService internalQueryUserService;

    // TASK Id를 통해 TASK를 조회
    public Task getTaskByTaskId(Long taskId) {

        Task task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new TaskException(TaskErrorCode.TASK_NOT_FOUND));

        return task;
    }

    // TASK ID를 통해 존재 유무 확인
    public void validateTaskExists(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND);
        }
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
        Long myTasksToday = taskRepository.countByAssigneeIdAndDueDateBetween(LoginUserId, startOfDay, endOfDay);
        Long teamProgress = totalTasks == 0 ? 0 : ((completedTasks + inProgressTasks) / totalTasks) * 100;
        Long completionRate = totalTasks == 0 ? 0 : (completedTasks / totalTasks * 100);

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

    // 검색어로 Task 조회 후 반환
    public List<TaskResponse<AssigneeSummaryResponse>> searchTasksByQuery(String query) {
        List<Task> found = taskRepository.findByTitleContainingIgnoreCase(query);

        List<TaskResponse<AssigneeSummaryResponse>> tasks = found.stream()
                .map(task -> {
                    AssigneeSummaryResponse assigneeResponse =
                            internalQueryUserService.getAssigneeSummaryByUserId(task.getAssigneeId());

                    return taskMapper.toTaskResponse(task, assigneeResponse);
                })
                .toList();

        return tasks;
    }

    public Long getTeamsProgress(List<Long> memberIdsByTeamId) {

        List<Task> tasks = taskRepository.findByAssigneeIdIn(memberIdsByTeamId);

        Long todoAndInProgressCount = 0L;
        Long doneCount = 0L;

        for (Task task : tasks) {
            if (task.getStatus().equals(Status.DONE)) {
                doneCount++;
            } else {
                todoAndInProgressCount++;
            }
        }

        Long teamProgress = (todoAndInProgressCount + doneCount) == 0
                ? 0
                : (long) ((double) doneCount / (todoAndInProgressCount + doneCount) * 100);

        return teamProgress;
    }
}
