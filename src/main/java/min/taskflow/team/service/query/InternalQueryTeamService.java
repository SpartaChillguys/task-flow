package min.taskflow.team.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.TeamSearchResponse;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamErrorCode;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InternalQueryTeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final InternalQueryUserService internalQueryUserService;

    // 검색 시 팀 정보 가져오기
    public List<TeamSearchResponse> searchTeamByQuery(String query) {

        if (query == null || query.isBlank()) {
            throw new TeamException(TeamErrorCode.INVALID_QUERY);
        }

        List<Team> found = teamRepository.findByNameContainingIgnoreCase(query);

        List<TeamSearchResponse> teams = found.stream()
                .map(team -> {
                    return teamMapper.toTeamSearchResponse(team);
                })
                .toList();

        return teams;
    }

    // 멤버 id
    public List<Long> getMembersIdByUserId(Long userId) {

        if (userId == null || userId <= 0) {
            throw new TeamException(TeamErrorCode.INVALID_USER_ID);
        }

        User user = internalQueryUserService.getUserByUserId(userId);

        Team team = user.getTeam();
        if (team == null) {
            throw new TeamException(TeamErrorCode.TEAM_NOT_FOUND);
        }

        return teamMapper.toMemberIdList(team.getMembers());
    }

    public Map<String, List<Long>> getTeamsProgress() {

        List<Team> teams = teamRepository.findAll();

        Map<String, List<Long>> response = new HashMap<>();
        for (Team team : teams) {

            List<Long> teamMemberIds = teamRepository.findMemberIdsByTeamId(team.getTeamId());

            response.put(team.getName(), teamMemberIds);
        }

        return response;
    }

    public List<Long> getMemberIdsByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        return teamMapper.toMemberIdList(team.getMembers());
    }
}
