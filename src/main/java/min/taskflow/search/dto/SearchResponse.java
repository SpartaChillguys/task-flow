package min.taskflow.search.dto;

import lombok.Builder;
import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;

import java.util.List;

@Builder
public record SearchResponse(
        List<TaskResponse> tasks,
        List<UserSearchAndAssigneeResponse> users,
        List<TeamSearchResponse> teams
) {}
