package min.taskflow.search.dto;

import lombok.Builder;
import min.taskflow.task.dto.response.TaskResponse;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;

import java.util.List;

@Builder
public record SearchResponse(
        List<TaskResponse<AssigneeSummaryResponse>> tasks,
        List<UserSearchAndAssigneeResponse> users,
        List<TeamSearchResponse> teams
) {
}
