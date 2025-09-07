package min.taskflow.search;

import min.taskflow.search.dto.SearchResponse;
import min.taskflow.search.service.ExternalQuerySearchService;
import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;
import min.taskflow.task.service.queryService.ExternalQueryTaskService;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.team.service.TeamService;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.service.queryService.ExternalQueryUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    private ExternalQuerySearchService externalQuerySearchService;

    @Mock
    private ExternalQueryTaskService externalQueryTaskService;

    @Mock
    private ExternalQueryUserService externalQueryUserService;

    @Mock
    private TeamService teamService;

    @Test
    void searchAll_returnsMappedResults() {
        // given
        String query = "관리자";

        TaskResponse taskResponse = new TaskResponse(1L, "사용자 인증 구현", "JWT 인증 시스템 구현", null,
                Priority.HIGH, Status.DONE, 1L, null, null, null);

        UserSearchAndAssigneeResponse userResponse =
                new UserSearchAndAssigneeResponse(1L, "admin", "관리자", "admin@example.com");

        TeamSearchResponse teamResponse =
                new TeamSearchResponse(1L, "개발팀", "프론트엔드 및 백엔드 개발자들");

        // mock service behavior
        when(externalQueryTaskService.searchTasksByQuery(query)).thenReturn(List.of(taskResponse));
        when(externalQueryUserService.searchUsersByQuery(query)).thenReturn(List.of(userResponse));
        when(teamService.searchTeamsByQuery(query)).thenReturn(List.of(teamResponse));

        // when
        SearchResponse result = externalQuerySearchService.searchAll(query);

        // then
        assertNotNull(result);
        assertEquals(1, result.tasks().size());
        assertEquals(1, result.users().size());
        assertEquals(1, result.teams().size());

        verify(externalQueryTaskService).searchTasksByQuery(query);
        verify(externalQueryUserService).searchUsersByQuery(query);
        verify(teamService).searchTeamsByQuery(query);
    }
}


