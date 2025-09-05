package min.taskflow.user.controller;


import lombok.RequiredArgsConstructor;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
import min.taskflow.user.service.queryService.ExternalQuueryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
유저관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ExternalQuueryUserService externalQuueryUserService;

    //현재 로그인중인 사용자의 id를 받아 프로필조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe(@Auth AuthUser authUser) {

        UserProfileResponse myprofile = externalQuueryUserService.getMe(authUser.getId());

        return ApiResponse.success(myprofile, "사용자 정보를 조회했습니다.");
    }

}
