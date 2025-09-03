package min.taskflow.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.dto.request.SignInRequest;
import min.taskflow.auth.dto.request.SignupRequest;
import min.taskflow.auth.dto.response.SignInResponse;
import min.taskflow.auth.dto.response.SignupResponse;
import min.taskflow.auth.service.ExternalAuthService;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.dto.AuthUser;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 유저 회원가입, 로그인 , 로그아웃 등 유저 관리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final ExternalAuthService externalAuthService;


    //회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = externalAuthService.signup(request);
        return ApiResponse.success(response, "회원가입에 성공하셨습니다");
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SignInResponse>> signin(@Valid @RequestBody SignInRequest request) {


        SignInResponse response = externalAuthService.signin(request);
        return ApiResponse.success(response, "로그인에 성공하셨습니다.");
    }

    @GetMapping("/test")
    public void test(@Auth AuthUser authUser) {
        System.out.println(authUser.getId());
    }
}
