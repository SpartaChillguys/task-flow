package min.taskflow.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.dto.SignupRequest;
import min.taskflow.auth.dto.SignupResponse;
import min.taskflow.auth.service.ExternalAuthService;
import min.taskflow.common.response.ApiResponse;
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
@RequestMapping("/auth")
public class AuthController {

    private final ExternalAuthService externalUserService;

    //회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = externalUserService.signup(request);
        return ApiResponse.success(response, "회원가입에 성공하셨습니다");
    }
}
