package min.taskflow.search;

import min.taskflow.search.dto.SearchResponse;
import min.taskflow.search.mapper.SearchMapper;
import min.taskflow.search.service.ExternalQuerySearchService;
import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.task.repository.TaskRepository;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.team.entity.Team;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.repository.UserRepository;
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
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private SearchMapper searchMapper;

    @Test
    void searchAll_returnsMappedResults() {
        // given
        String query = "관리자";

        User user = User.builder()
                .userName("admin")
                .name("관리자")
                .email("admin@example.com")
                .password("password")
                .role(UserRole.ADMIN)
                .build();

        Task task = Task.builder()
                .title("사용자 인증 구현")
                .description("JWT 인증 시스템 구현")
                .dueDate(null)
                .priority(Priority.HIGH)
                .status(Status.DONE)
                .assigneeId(user.getUserId())
                .build();

        Team team = Team.builder()
                .name("개발팀")
                .description("프론트엔드 및 백엔드 개발자들")
                .build();


        TaskResponse taskResponse = new TaskResponse(1L, "사용자 인증 구현", "JWT 인증 시스템 구현", null, Priority.HIGH, Status.DONE, user.getUserId(), null, null, null);
        UserSearchAndAssigneeResponse userResponse = new UserSearchAndAssigneeResponse(user.getUserId(), user.getUserName(), user.getName(), user.getEmail());
        TeamSearchResponse teamResponse = new TeamSearchResponse(team.getTeamId(), team.getName(), team.getDescription());

        // mock repository behavior
        when(taskRepository.findByTitleContainingIgnoreCase(query)).thenReturn(List.of(task));
        when(userRepository.findByNameContainingIgnoreCaseOrUserNameContainingIgnoreCase(query, query)).thenReturn(List.of(user));
        when(teamRepository.findByNameContainingIgnoreCase(query)).thenReturn(List.of(team));

        // mock mapper behavior
        when(searchMapper.toTaskResponseList(anyList(), anyMap())).thenReturn(List.of(taskResponse));
        when(searchMapper.toUserResponseList(anyList())).thenReturn(List.of(userResponse));
        when(searchMapper.toTeamResponseList(anyList())).thenReturn(List.of(teamResponse));

        // when
        SearchResponse result = externalQuerySearchService.searchAll(query);

        // then
        assertNotNull(result);
        assertEquals(1, result.tasks().size());
        assertEquals(1, result.users().size());
        assertEquals(1, result.teams().size());

        verify(taskRepository).findByTitleContainingIgnoreCase(query);
        verify(userRepository).findByNameContainingIgnoreCaseOrUserNameContainingIgnoreCase(query, query);
        verify(teamRepository).findByNameContainingIgnoreCase(query);
        verify(searchMapper).toTaskResponseList(anyList(), anyMap());
        verify(searchMapper).toUserResponseList(anyList());
        verify(searchMapper).toTeamResponseList(anyList());
    }
}

