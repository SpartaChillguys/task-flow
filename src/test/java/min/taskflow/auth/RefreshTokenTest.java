package min.taskflow.auth;

import min.taskflow.auth.exception.AuthErrorCode;
import min.taskflow.auth.exception.AuthException;
import min.taskflow.auth.jwt.JwtUtil;
import min.taskflow.auth.service.commandService.ExternalCommandAuthService;
import min.taskflow.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {

    @InjectMocks
    private ExternalCommandAuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    private String invalidRefreshToken = "invalid_refresh_token";

    @Test
    void refreshAccessToken_실패_INVALID_REFRESH_TOKEN발생() {

        // given
        when(jwtUtil.parseUserInfo(invalidRefreshToken))
                .thenThrow(new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        // when & then
        assertThatThrownBy(() -> authService.refreshAccessToken(invalidRefreshToken))
                .isInstanceOf(AuthException.class)
                .extracting("errorCode")
                .isEqualTo(AuthErrorCode.INVALID_REFRESH_TOKEN);
        
        verify(userRepository, never()).findById(any());
        verify(jwtUtil, never()).createAccessToken(any(), any());
    }
}
