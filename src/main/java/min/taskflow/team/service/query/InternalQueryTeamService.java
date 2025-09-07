package min.taskflow.team.service.query;

import lombok.RequiredArgsConstructor;
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
public class InternalQueryTeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final InternalQueryUserService internalQueryUserService;

    // 검색 시 팀 정보 가져오기
    public List<TeamResponse> searchTeamByQuery(String query) {

        List<Team> teams = teamRepository.findByNameContainingIgnoreCase(query);

        return teamMapper.toTeamResponseList(teams);
    }

    // 멤버 id
    public List<Long> getMembersIdByUserId(Long userId) {

        User user = internalQueryUserService.getUserByUserId(userId);

        Team team = user.getTeam();
        if (team == null) {
            throw new TeamException(TeamErrorCode.TEAM_NOT_FOUND);
        }

        return teamMapper.toMemberIdList(team.getMembers());
    }
}
