package min.taskflow.user.controller;


import lombok.RequiredArgsConstructor;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.queryService.ExternalQuueryUserService;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final ExternalQuueryUserService externalQuueryUserService;
    private final InternalQueryUserService internalQueryUserService;

    //현재 로그인중인 사용자의 id를 받아 프로필조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@Auth AuthUser authUser) {

        UserResponse myprofile = externalQuueryUserService.getMe(authUser.getId());

        return ApiResponse.success(myprofile, "사용자 정보를 조회했습니다.");
    }

    /**
     * 특정 유저 조회 (id 기준)
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = internalQueryUserService.findByUserId(id);
        return ResponseEntity.ok(internalQueryUserService.toUserResponse(user));
    }
    
    /**
     * 전체 유저 조회
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(internalQueryUserService.findAllUsers());
    }

    /**
     * 전체 유저 이름만 조회
     * GET /api/users/names
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllUserNames() {
        return ResponseEntity.ok(internalQueryUserService.findAllUserNames());
    }

}
