package min.taskflow.team.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;
import min.taskflow.team.exception.TeamException;
import min.taskflow.team.mapper.TeamMapper;
import min.taskflow.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static min.taskflow.team.exception.TeamErrorCode.TEAM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    // 팀 생성
    @Transactional
    public TeamResponse createTeam(TeamCreateRequest request) {

        if (teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
        }

        Team team = TeamMapper.toEntity(request);
        teamRepository.save(team);

        return TeamMapper.toResponse(team);
    }

    // 팀 단건 조회
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(
                        ()-> new TeamException(TEAM_NOT_FOUND)
                );
        return TeamMapper.toResponse(team);
    }

    // 팀 전체 조회
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {

        return teamRepository.findAll().stream()
                .map(TeamMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 팀 수정
    @Transactional
    public TeamResponse updateTeam(Long teamId, TeamUpdateRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new TeamException(TEAM_NOT_FOUND)
                );

        if (!team.getName().equals(request.getName()) && teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 팀입니다.");
        }

        TeamMapper.updateEntity(team, request);
        return TeamMapper.toResponse(team);
    }

    // 팀 삭제
    @Transactional
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new TeamException(TEAM_NOT_FOUND)
                );
        team.delete();
        teamRepository.save(team);
    }
}
