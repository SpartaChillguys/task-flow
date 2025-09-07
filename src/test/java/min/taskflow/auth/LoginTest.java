package min.taskflow.auth;

import min.taskflow.auth.config.PasswordEncoder;
import min.taskflow.auth.dto.info.TokenInfo;
import min.taskflow.auth.dto.request.LoginRequest;
import min.taskflow.auth.exception.AuthErrorCode;
import min.taskflow.auth.exception.AuthException;
import min.taskflow.auth.jwt.JwtUtil;
import min.taskflow.auth.service.commandService.ExternalCommandAuthService;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ExternalCommandAuthService authService;

    //테스트용
    private User testUser;
    private LoginRequest validLoginRequest;
    private String testUsername = "testuser";
    private String testPassword = "password123";
    private String encodedPassword = "encoded_password_123";
    private String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName(testUsername)
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();

        validLoginRequest = new LoginRequest(testUsername, testPassword);
    }

    @Test
    void login_로그인시_정상적인플로우() {
        // given
        when(userRepository.findByUserName(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.createAccessToken(testUser.getUserId(), testUser.getRole())).thenReturn(jwtToken);

        // when
        TokenInfo tokenInfo = authService.login(validLoginRequest);

        // then
        assertThat(tokenInfo).isNotNull();
        assertThat(tokenInfo.accessToken()).isEqualTo(jwtToken);

        // 메서드 호출 검증
        verify(userRepository, times(1)).findByUserName(testUsername);
        verify(passwordEncoder, times(1)).matches(testPassword, encodedPassword);
        verify(jwtUtil, times(1)).createAccessToken(testUser.getUserId(), testUser.getRole());
    }

    @Test
    void login_실패_WRONG_USERNAME발생() {
        // when
        when(userRepository.findByUserName(testUsername)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthException.class)
                .extracting("errorCode")
                .isEqualTo(AuthErrorCode.WRONG_USERNAME);

        // 비밀번호 검증과 토큰 생성은 호출되지 않아야 함
        verify(userRepository, times(1)).findByUserName(testUsername);
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(jwtUtil, times(0)).createAccessToken(any(), any());
    }


    @Test
    void login_실패_WRONG_PASSWORD발생() {
        // when
        when(userRepository.findByUserName(testUsername)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, encodedPassword)).thenReturn(false);

        // then
        assertThatThrownBy(() -> authService.login(validLoginRequest))
                .isInstanceOf(AuthException.class)
                .extracting("errorCode")
                .isEqualTo(AuthErrorCode.WRONG_PASSWORD);

        // 토큰 생성은 호출되지 않아야 함
        verify(userRepository, times(1)).findByUserName(testUsername);
        verify(passwordEncoder, times(1)).matches(testPassword, encodedPassword);
        verify(jwtUtil, times(0)).createAccessToken(any(), any());
    }
}
