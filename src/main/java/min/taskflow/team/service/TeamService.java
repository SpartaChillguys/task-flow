package min.taskflow.team.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamMemberResponse;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamErrorCode;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import min.taskflow.user.entity.User;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

    // 팀 생성
    @Transactional
    public TeamResponse createTeam(TeamCreateRequest request) {

        if (teamRepository.existsByName(request.name())) {
            throw new TeamException(TeamErrorCode.DUPLICATE_TEAM_NAME);
        }

        Team team = teamMapper.toEntity(request);
        teamRepository.save(team);

        return teamMapper.toTeamResponse(team);
    }

    // 팀 단건 조회
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        return teamMapper.toTeamResponse(team);
    }

    // 팀 전체 조회
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {

        List<Team> teams = teamRepository.findAllWithMembers();
        return teams.stream()
                .map(teamMapper::toTeamResponse)
                .toList();
    }

    // 팀 수정
    @Transactional
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
    @Transactional
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        team.delete();
    }

    // 팀 멤버 조회
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        return teamMapper.toTeamMemberResponseList(team.getMembers());
    }

    // 팀 멤버 추가
    @Transactional
    public TeamMemberResponse addMemberById(Long teamId, Long memberId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.MEMBER_NOT_FOUND));

        if (team.getMembers().contains(member)) {
            throw new TeamException(TeamErrorCode.MEMBER_ALREADY_IN_TEAM);
        }

        team.addMember(member);
        userRepository.save(member);

        return teamMapper.toTeamMemberResponse(member);
    }

    // 팀 멤버 삭제
    @Transactional
    public void removeMemberId(Long teamId, Long memberId) {

        Team team = teamRepository.findByIdWithMembers(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.MEMBER_NOT_FOUND));

        if (!team.getMembers().contains(member)) {
            throw new TeamException(TeamErrorCode.MEMBER_NOT_IN_TEAM);
        }

        team.removeMember(member);
    }

    // 소속 없는 팀 조회
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getAvailableMembers() {

        List<User> allusers = userRepository.findAll();

        List<User> availableUsers = allusers.stream()
                .filter(user -> user.getTeam() == null && !user.isDeleted())
                .toList();

        return teamMapper.toTeamMemberResponseList(availableUsers);
    }
}
