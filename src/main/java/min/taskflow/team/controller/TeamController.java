package min.taskflow.team.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.team.dto.*;
import min.taskflow.team.service.command.ExternalCommandTeamService;
import min.taskflow.team.service.query.ExternalQueryTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final ExternalCommandTeamService externalCommandTeamService;
    private final ExternalQueryTeamService externalQueryTeamService;

    // 팀 목록 조회
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {

        List<TeamResponse> responseList = externalQueryTeamService.getAllTeams();

        return ApiResponse.success(responseList, "팀 전체 조회 성공했습니다.");
    }

    // 특정 팀 조회
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable Long teamId) {

        TeamResponse response = externalQueryTeamService.getTeamById(teamId);

        return ApiResponse.success(response, "팀 단건 조회 성공했습니다.");
    }

    // 팀 멤버 목록 조회
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getTeamMembers(@PathVariable Long teamId) {

        List<MemberResponse> members = externalQueryTeamService.getTeamMembers(teamId);

        return ApiResponse.success(members, "팀 멤버 조회에 성공하셨습니다.");
    }

    // 팀 생성
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@RequestBody TeamCreateRequest request) {

        TeamResponse response = externalCommandTeamService.createTeam(request);

        return ApiResponse.created(response, "팀이 성공적으로 생성되었습니다.");
    }

    // 팀 정보 수정
    @PutMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable Long teamId,
                                                                @RequestBody TeamUpdateRequest request) {

        TeamResponse response = externalCommandTeamService.updateTeam(teamId, request);

        return ApiResponse.success(response, "팀 정보가 성공적으로 수정되었습니다.");
    }

    // 팀 삭제
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {

        externalCommandTeamService.deleteTeam(teamId);

        return ApiResponse.noContent("팀이 성공적으로 삭제되었습니다.");
    }

    // 팀 멤버 추가
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(@PathVariable Long teamId,
                                                                 @RequestBody MemberAddRequest memberAddRequest) {

        MemberResponse response = externalCommandTeamService.addMemberById(teamId, memberAddRequest.userId());

        return ApiResponse.created(response, "팀 멤버가 성공적으로 추가되었습니다.");
    }

    // 팀 멤버 제거
    @DeleteMapping("/teams/{teamId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long teamId,
                                                          @PathVariable Long userId) {

        externalCommandTeamService.removeMemberId(teamId, userId);

        return ApiResponse.noContent("팀 멤버가 성공적으로 삭제되었습니다.");
    }

    // 소속 없는 멤버 조회
    @GetMapping("/users/available")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAvailableMembers(@PathVariable(required = false) Long teamId) {

        List<MemberResponse> availableMembers = externalQueryTeamService.getAvailableMembers(teamId);

        return ApiResponse.success(availableMembers, "팀에 속하지 않은 사용자 목록을 조회 성공했습니다.");
    }
}
