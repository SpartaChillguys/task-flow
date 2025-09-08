package min.taskflow.user.controller;


import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.service.query.ExternalQueryUserService;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
유저관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ExternalQueryUserService externalQueryUserService;
    private final InternalQueryUserService internalQueryUserService;

    //현재 로그인중인 사용자의 id를 받아 프로필조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe(@AuthenticationPrincipal Long userId) {

        UserProfileResponse myprofile = externalQueryUserService.getMe(userId);

        return ApiResponse.success(myprofile, "사용자 정보를 조회했습니다.");
    }

    /**
     * 전체 유저 조회
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ApiResponse.success(
                internalQueryUserService.findAllUsersAsResponse(),
                "요청이 성공적으로 처리되었습니다."
        );
    }

}
