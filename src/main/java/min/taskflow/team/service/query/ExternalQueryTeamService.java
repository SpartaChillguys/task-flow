package min.taskflow.team.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.MemberResponse;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamErrorCode;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryTeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final InternalQueryUserService internalQueryUserService;

    // 팀 단건 조회
    public TeamResponse getTeamById(Long teamId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        return teamMapper.toTeamResponse(team);
    }

    // 팀 전체 조회
    public List<TeamResponse> getAllTeams() {

        List<Team> teams = teamRepository.findAllWithMembers();
        return teamMapper.toTeamResponseList(teams);
    }

    // 팀 멤버 조회
    public List<MemberResponse> getTeamMembers(Long teamId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));
        return teamMapper.toMemberResponseList(team.getMembers());
    }

    // 소속 없는 팀 조회
    public List<MemberResponse> getAvailableMembers() {

        List<User> allUsers = internalQueryUserService.findAllUsers();

        List<User> availableUsers = allUsers.stream()
                .filter(user -> user.getTeam() == null && !user.isDeleted())
                .toList();

        return teamMapper.toMemberResponseList(availableUsers);
    }
}
