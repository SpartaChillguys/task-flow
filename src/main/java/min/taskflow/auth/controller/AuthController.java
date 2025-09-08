package min.taskflow.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.dto.info.TokenInfo;
import min.taskflow.auth.dto.request.DeleteRequest;
import min.taskflow.auth.dto.request.LoginRequest;
import min.taskflow.auth.dto.request.RegisterRequest;
import min.taskflow.auth.dto.response.RegisterResponse;
import min.taskflow.auth.jwt.CookieUtil;
import min.taskflow.auth.service.commandService.ExternalCommandAuthService;
import min.taskflow.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
 유저 회원가입, 로그인 , 로그아웃 등 유저 관리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ExternalCommandAuthService externalCommandAuthService;
    private final CookieUtil cookieUtil;

    //회원가입 API
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = externalCommandAuthService.register(request);

        return ApiResponse.success(response, "회원가입에 성공하셨습니다");
    }

    //로그인 API
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request,
                                                                  HttpServletResponse httpServletResponse) {

        TokenInfo tokenInfo = externalCommandAuthService.login(request);
        cookieUtil.addHttpOnlyCookie(httpServletResponse, tokenInfo.refreshToken());

        return ApiResponse.success(Map.of("token", tokenInfo.accessToken()), "로그인에 성공하셨습니다.");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse httpServletResponse) {

        cookieUtil.deleteCookie(httpServletResponse);

        return ApiResponse.success(null, "로그아웃 되었습니다.");
    }

    //계정 삭제(회원 탈퇴)
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal Long userId, @Valid @RequestBody DeleteRequest request) {

        externalCommandAuthService.delete(userId, request);

        return ApiResponse.noContent("회원탈퇴가 완료되었습니다.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request) {

        String refreshToken = cookieUtil.getCookieValue(request);
        String newAccessToken = externalCommandAuthService.refreshAccessToken(refreshToken);

        return ApiResponse.success(newAccessToken, "토큰이 재발급 되었습니다.");
    }
}
