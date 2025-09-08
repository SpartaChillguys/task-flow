package min.taskflow.team;

import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.team.service.command.ExternalCommandTeamService;
import min.taskflow.team.service.query.ExternalQueryTeamService;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.service.command.InternalCommandUserService;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static min.taskflow.team.exception.TeamErrorCode.TEAM_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private InternalCommandUserService internalCommandUserService;

    @Mock
    private InternalQueryUserService internalQueryUserService;

    @InjectMocks
    private ExternalCommandTeamService externalCommandTeamService;

    @InjectMocks
    private ExternalQueryTeamService externalQueryTeamService;

    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        user = User.builder()
                .userName("chulsoo")
                .email("chulsoo@example.com")
                .password("1234")
                .name("철수")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "userId", 1L);
    }

    @Test
    void 팀을_성공적으로_생성했습니다() {

        TeamCreateRequest request = new TeamCreateRequest("개발팀", "백엔드/프론트");
        TeamResponse mockResponse = new TeamResponse(
                1L,
                "개발팀",
                "백엔드/프론트",
                LocalDateTime.now(),
                Collections.emptyList()
        );

        when(teamRepository.existsByName(request.name())).thenReturn(false);
        when(teamMapper.toEntity(request)).thenReturn(team);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(teamMapper.toTeamResponse(team)).thenReturn(mockResponse);

        TeamResponse response = externalCommandTeamService.createTeam(request);

        assertEquals("개발팀", response.name());
        assertEquals("백엔드/프론트", response.description());
    }

    @Test
    void 존재하지_않는_팀_조회() {
        when(teamRepository.findByIdWithMembers(1L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> externalQueryTeamService.getTeamById(1L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_조회합니다() {
        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findByIdWithMembers(1L)).thenReturn(Optional.of(team));
        when(teamMapper.toTeamResponse(team)).thenReturn(
                new TeamResponse(
                        team.getTeamId(),
                        team.getName(),
                        team.getDescription(),
                        LocalDateTime.now(),
                        Collections.emptyList()
                )
        );

        TeamResponse response = externalQueryTeamService.getTeamById(1L);

        assertEquals("개발팀", response.name());
        assertEquals("백엔드/프론트", response.description());
    }

    @Test
    void 팀을_성공적으로_수정합니다() {
        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.existsByName("디자인팀")).thenReturn(false);
        doNothing().when(teamMapper).updateEntity(team, new TeamUpdateRequest("디자인팀", "UI/UX 담당"));
        when(teamMapper.toTeamResponse(team)).thenReturn(
                new TeamResponse(
                        team.getTeamId(),
                        "디자인팀",
                        "UI/UX 담당",
                        LocalDateTime.now(),
                        Collections.emptyList()
                )
        );

        TeamUpdateRequest request = new TeamUpdateRequest("디자인팀", "UI/UX 담당");
        TeamResponse response = externalCommandTeamService.updateTeam(1L, request);

        assertEquals("디자인팀", response.name());
        assertEquals("UI/UX 담당", response.description());
    }

    @Test
    void 존재하지_않는_팀은_수정할_수_없습니다() {

        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        TeamUpdateRequest request = new TeamUpdateRequest("디자인팀", "UI/UX 담당");

        TeamException exception = assertThrows(TeamException.class,
                () -> externalCommandTeamService.updateTeam(999L, request));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_삭제합니다() {

        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        externalCommandTeamService.deleteTeam(1L);

        assertTrue(team.isDeleted());
        assertNotNull(team.getDeletedAt());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void 존재하지_않는_팀은_삭제할_수_없습니다() {

        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> externalCommandTeamService.deleteTeam(999L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀에_멤버를_성공적으로_추가합니다() {
        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        User user = User.builder()
                .userName("chulsoo")
                .email("chulsoo@example.com")
                .password("1234")
                .name("철수")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "userId", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(internalQueryUserService.getUserByUserId(1L)).thenReturn(user);

        externalCommandTeamService.addMemberById(1L, 1L);

        assertEquals(team, user.getTeam());  // 유저가 팀에 속했는지 확인
        assertTrue(team.getMembers().contains(user)); // 팀 멤버 목록에 포함됐는지 확인
    }

    @Test
    void 존재하지_않는_팀에_멤버를_추가할_수_없습니다() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> externalCommandTeamService.addMemberById(999L, 1L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀에서_멤버를_성공적으로_삭제합니다() {
        Team team = Team.builder()
                .name("개발팀")
                .description("백엔드/프론트")
                .build();
        ReflectionTestUtils.setField(team, "teamId", 1L);

        User user = User.builder()
                .userName("younghee")
                .email("younghee@example.com")
                .password("1234")
                .name("영희")
                .role(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "userId", 2L);

        // 팀과 유저 관계 미리 설정
        user.assignTeam(team);
        team.addMember(user);

        when(teamRepository.findByIdWithMembers(1L)).thenReturn(Optional.of(team));
        when(internalQueryUserService.getUserByUserId(2L)).thenReturn(user);

        externalCommandTeamService.removeMemberId(1L, 2L);

        assertNull(user.getTeam()); // 유저의 팀이 해제됐는지 확인
        assertFalse(team.getMembers().contains(user)); // 팀 멤버 목록에서 제거됐는지 확인
    }

    @Test
    void 존재하지_않는_팀에서_멤버를_삭제할_수_없습니다() {

        when(teamRepository.findByIdWithMembers(999L)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class,
                () -> externalCommandTeamService.removeMemberId(999L, 1L));

        assertEquals(TEAM_NOT_FOUND, exception.getErrorCode());
    }
}
