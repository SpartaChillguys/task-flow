package min.taskflow.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.dto.request.DeleteRequest;
import min.taskflow.auth.dto.request.LoginRequest;
import min.taskflow.auth.dto.request.RegisterRequest;
import min.taskflow.auth.dto.response.LoginResponse;
import min.taskflow.auth.dto.response.RegisterResponse;
import min.taskflow.auth.service.commandService.ExternalCommandAuthService;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 유저 회원가입, 로그인 , 로그아웃 등 유저 관리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final ExternalCommandAuthService externalCommandAuthService;


    //회원가입 API
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = externalCommandAuthService.register(request);
        return ApiResponse.success(response, "회원가입에 성공하셨습니다");
    }

    //로그인 API
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = externalCommandAuthService.login(request);
        return ApiResponse.success(response, "로그인에 성공하셨습니다.");
    }

    //계정 삭제(회원 탈퇴)
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> delete(@Auth AuthUser authUser, @Valid @RequestBody DeleteRequest request) {

        externalCommandAuthService.delete(authUser, request);
        return ApiResponse.noContent("회원탈퇴가 완료되었습니다.");
    }
}
