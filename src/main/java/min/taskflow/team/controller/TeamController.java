package min.taskflow.team.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamMemberResponse;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.service.TeamService;
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
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@RequestBody TeamCreateRequest request) {

        TeamResponse response = teamService.createTeam(request);
        return ApiResponse.created(response, "팀이 성공적으로 생성되었습니다.");
    }

    // 팀 단건 조회
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable Long teamId) {

        TeamResponse response = teamService.getTeamById(teamId);
        return ApiResponse.success(response, "팀 단건 조회 성공했습니다.");
    }

    // 팀 전체 조회
    @GetMapping("/teams-names")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {

        List<TeamResponse> responseList = teamService.getAllTeams();
        return ApiResponse.success(responseList, "팀 전체 조회 성공했습니다.");
    }

    // 팀 수정
    @PutMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable Long teamId,
                                                                @RequestBody TeamUpdateRequest request) {

        TeamResponse response = teamService.updateTeam(teamId, request);
        return ApiResponse.success(response, "팀 정보가 성공적으로 수정되었습니다.");
    }

    // 팀 삭제
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {

        teamService.deleteTeam(teamId);
        return ApiResponse.noContent("팀이 성공적으로 삭제되었습니다.");
    }

    // 팀 멤버 조회
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMembers(@PathVariable Long teamId) {

        List<TeamMemberResponse> members = teamService.getTeamMembers(teamId);
        return ApiResponse.success(members, "팀 멤버 조회에 성공하셨습니다.");
    }

    // 팀 멤버 추가
    @PostMapping("/teams/{teamId}/members/{memberId}")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> addMember(@PathVariable Long teamId,
                                                                     @PathVariable Long memberId) {

        TeamMemberResponse response = teamService.addMemberById(teamId, memberId);
        return ApiResponse.created(response, "팀 멤버가 성공적으로 추가되었습니다.");
    }

    // 팀 멤버 삭제
    @DeleteMapping("/teams/{teamId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long teamId,
                                                          @PathVariable Long memberId) {

        teamService.removeMemberId(teamId, memberId);
        return ApiResponse.noContent("팀 멤버가 성공적으로 삭제되었습니다.");
    }
}
