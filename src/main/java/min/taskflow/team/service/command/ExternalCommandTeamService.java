package min.taskflow.team.service.command;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.MemberResponse;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamErrorCode;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.command.InternalCommandUserService;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalCommandTeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final InternalQueryUserService internalQueryUserService;
    private final InternalCommandUserService internalCommandUserService;

    // 팀 생성
    public TeamResponse createTeam(TeamCreateRequest request) {

        if (teamRepository.existsByName(request.name())) {
            throw new TeamException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        Team team = teamMapper.toEntity(request);
        teamRepository.save(team);

        return teamMapper.toTeamResponse(team);
    }

    // 팀 수정
    public TeamResponse updateTeam(Long teamId, TeamUpdateRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        if (!team.getName().equals(request.name()) && teamRepository.existsByName(request.name())) {
            throw new TeamException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        teamMapper.updateEntity(team, request);

        return teamMapper.toTeamResponse(team);
    }

    // 팀 삭제
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        team.delete();
    }

    // 팀 멤버 추가
    public MemberResponse addMemberById(Long teamId, Long memberId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));
        User member = internalQueryUserService.getUserByUserId(memberId);

        if (team.getMembers().contains(member)) {
            throw new TeamException(TeamErrorCode.MEMBER_ALREADY_IN_TEAM);
        }

        team.addMember(member);

        internalCommandUserService.saveUserInfo(member);

        return teamMapper.toMemberResponse(member);
    }

    // 팀 멤버 삭제
    public void removeMemberId(Long teamId, Long memberId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        User member = internalQueryUserService.getUserByUserId(memberId);

        if (!team.getMembers().contains(member)) {
            throw new TeamException(TeamErrorCode.MEMBER_NOT_IN_TEAM);
        }

        team.removeMember(member);
    }
}
