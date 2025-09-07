package min.taskflow.search.mapper;

import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.task.entity.Task;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.team.entity.Team;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SearchMapper {

    public TaskResponse toTaskResponse(Task task, User assignee) {

        return TaskResponse.builder()
                .id(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(task.getAssigneeId())
                .assigneeResponse(assignee == null ? null : toUserResponse(assignee))
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public UserSearchAndAssigneeResponse toUserResponse(User user) {

        return UserSearchAndAssigneeResponse.builder()
                .userid(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public TeamSearchResponse toTeamResponse(Team team) {

        return TeamSearchResponse.builder()
                .id(team.getTeamId())
                .name(team.getName())
                .description(team.getDescription())
                .build();
    }

    public List<TaskResponse> toTaskResponseList(List<Task> tasks, Map<Long, User> assigneeMap) {

        return tasks.stream()
                .map(task -> toTaskResponse(task, assigneeMap.get(task.getAssigneeId())))
                .toList();
    }

    public List<UserSearchAndAssigneeResponse> toUserResponseList(List<User> users) {

        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    public List<TeamSearchResponse> toTeamResponseList(List<Team> teams) {

        return teams.stream()
                .map(this::toTeamResponse)
                .toList();
    }
}
