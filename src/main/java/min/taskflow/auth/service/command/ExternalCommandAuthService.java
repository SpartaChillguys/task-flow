package min.taskflow.auth.service.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.config.PasswordEncoder;
import min.taskflow.auth.dto.info.TokenInfo;
import min.taskflow.auth.dto.info.UserInfo;
import min.taskflow.auth.dto.request.DeleteRequest;
import min.taskflow.auth.dto.request.LoginRequest;
import min.taskflow.auth.dto.request.RegisterRequest;
import min.taskflow.auth.dto.response.RegisterResponse;
import min.taskflow.auth.exception.AuthErrorCode;
import min.taskflow.auth.exception.AuthException;
import min.taskflow.auth.jwt.JwtUtil;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 사용자 인증 관련 로직(signup, signin , login)
 */
@Service
@RequiredArgsConstructor
public class ExternalCommandAuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 로직
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        // 이메일 중복 검증
        if (userRepository.existsByEmail((request.email()))) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
        // 유저이름 중복 검증
        if (userRepository.existsByUserName((request.username()))) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_USERNAME);
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 엔티티 변환
        User user = userMapper.toEntity(request, encodedPassword);
        User saveUser = userRepository.save(user);

        // 디티오 변환
        RegisterResponse userSaveResponse = userMapper.toRegistResponse(saveUser);

        return userSaveResponse;
    }

    // 로그인 로직
    @Transactional
    public TokenInfo login(@Valid LoginRequest request) {

        // 유저네임 존재 검증
        User user = userRepository.findByUserName(request.username())
                .orElseThrow(() -> new AuthException(AuthErrorCode.WRONG_USERNAME));

        // 비번 일치 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.WRONG_PASSWORD);
        }

        // 액세스 토큰과 리프레시 토큰 발급
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getRole());

        return new TokenInfo(accessToken, refreshToken);
    }

    // 회원탈퇴 로직
    @Transactional
    public void delete(Long userId, DeleteRequest request) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.WRONG_PASSWORD);

        }

        user.delete();
    }

    // 액세스 토큰 재발급 로직
    public String refreshAccessToken(String refreshToken) {

        if (refreshToken == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        UserInfo userInfo = jwtUtil.parseUserInfo(refreshToken);

        return jwtUtil.createAccessToken(userInfo.userId(), userInfo.userRole());
    }
}
