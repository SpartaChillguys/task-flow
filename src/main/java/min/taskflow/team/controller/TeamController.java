package min.taskflow.team.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    // 팀 생성
    @PostMapping("/teams")
    public ResponseEntity<TeamResponse> createTeam(
            @RequestBody TeamCreateRequest request) {
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팀 단건 조회
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(
            @PathVariable Long teamId) {
        TeamResponse response = teamService.getTeamById(teamId);
        return ResponseEntity.ok(response);
    }

    // 팀 전체 조회
    @GetMapping("/teams-names")
    public ResponseEntity<List<TeamResponse>> getAllTeam() {
        List<TeamResponse> responseList = teamService.getAllTeams();
        return ResponseEntity.ok(responseList);
    }

    // 팀 수정
    @PutMapping("/teams/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamUpdateRequest request) {
        TeamResponse response = teamService.updateTeam(teamId, request);
        return ResponseEntity.ok(response);
    }

    // 팀 삭제
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<Void> deleteTeam(
            @PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
