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

        List<TeamResponse> response = teams.stream()
                .map(team -> {
                    return teamMapper.toTeamResponse(team);
                })
                .toList();

        return response;
    }

    // 팀 멤버 조회
    public List<MemberResponse> getTeamMembers(Long teamId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));
        return teamMapper.toMemberResponseList(team.getMembers());
    }

    // 소속 없는 팀 조회
    public List<MemberResponse> getAvailableMembers(Long teamId) {

        List<User> allUsers = internalQueryUserService.findAllUsers();

        // TODO: 지피티 썼으니 리펙토링하여 설계에 맞게 수정해야합니다.
        List<User> availableUsers = allUsers.stream()
                .filter(user -> {
                    // 삭제된 유저 제외
                    if (user.isDeleted()) return false;

                    if (teamId == null) {
                        // teamId 없으면 팀이 아예 없는 유저만
                        return user.getTeam() == null;
                    } else {
                        // teamId 있으면, 해당 팀이 아닌 유저만
                        return user.getTeam() == null || !user.getTeam().getTeamId().equals(teamId);
                    }
                })
                .toList();

        return teamMapper.toMemberResponseList(availableUsers);
    }
}
