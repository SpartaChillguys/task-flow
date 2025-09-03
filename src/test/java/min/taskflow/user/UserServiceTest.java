package min.taskflow.user;

import min.taskflow.auth.service.ExternalAuthService;
import min.taskflow.user.dto.UserSaveRequest;
import min.taskflow.user.dto.UserSaveResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private ExternalAuthService externalUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void signup_유저_저장시_정상적으로저장된다() {
        // given
        UserSaveRequest request = new UserSaveRequest(
                "testuser",
                "password123!",
                "test@example.com",
                "테스트유저"
        );

        String encodedPassword = passwordEncoder.encode(request.password());

        User mockUser = userMapper.toEntity(request, encodedPassword);


        UserSaveResponse expectedResponse = UserSaveResponse.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .name("테스트유저")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        // Mock 동작 설정
        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);
        Mockito.when(userRepository.existsByUserName(request.username())).thenReturn(false);
        Mockito.when(userMapper.toEntity(request, encodedPassword)).thenReturn(mockUser);
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);
        Mockito.when(userMapper.toDto(mockUser)).thenReturn(expectedResponse);

        // when
        UserSaveResponse result = externalUserService.signup(request);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.username()).isEqualTo("testuser");
        Assertions.assertThat(result.email()).isEqualTo("test@example.com");
        Assertions.assertThat(result.name()).isEqualTo("테스트유저");
        Assertions.assertThat(result.role()).isEqualTo(UserRole.USER);
    }

    @Test
    public void signup_아이디중복시_예외발생() {
        // given
        UserSaveRequest request = new UserSaveRequest(
                "testuser",
                "password123!",
                "test@example.com",
                "테스트유저"
        );

        //when
        Mockito.when(userRepository.existsByUserName("testuser")).thenReturn(true); // 중복

        //then
        Assertions.assertThatThrownBy(() -> externalUserService.signup(request))
                .isInstanceOf(UserException.class)
                .hasMessage("이미 존재하는 유저 이름입니다.");
    }

    @Test
    public void signup_이메일중복시_예외발생() {
        // given
        UserSaveRequest request = new UserSaveRequest(
                "testuser",
                "password123!",
                "test@example.com",
                "테스트유저"
        );

        // when
        Mockito.when(userRepository.existsByEmail("test@example.com")).thenReturn(true); // 중복

        // then
        Assertions.assertThatThrownBy(() -> externalUserService.signup(request))
                .isInstanceOf(UserException.class)
                .hasMessage("이미 존재하는 이메일 입니다.");
    }
}