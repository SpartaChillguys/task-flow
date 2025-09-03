package min.taskflow.team.service;

import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static min.taskflow.team.exception.TeamErrorCode.TEAM_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeamServiceTest {

    private TeamRepository teamRepository;
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class); // 레포지토리 모킹
        teamService = new TeamService(teamRepository); // 서비스 모킹
    }

    @Test
    public void team을_성공적으로_생성했습니다() {
        TeamCreateRequest request = new TeamCreateRequest("개발팀", "백엔드/프론트");
        Team savedTeam = Team.builder()
                .teamId(1L)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam); // save 동작 모킹입니다.

        TeamResponse response = teamService.createTeam(request);

        assertEquals("개발팀", response.getName());
        assertEquals("백엔드/프론트", response.getDescription());
    }

    @Test
    public void 존재하지_않는_팀_조회() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class, ()-> {
            teamService.getTeamById(1L);
        });

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }
}
