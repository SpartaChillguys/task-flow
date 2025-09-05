package min.taskflow.user;

import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import min.taskflow.user.service.queryService.ExternalUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    ExternalUserService externalUserService;

    @Test
    void getMe_요청시_성공적() {
        // given
        Long userId = 1L;
        User user = new User("test", "123456q!@", "test@test", "test", UserRole.USER, null);

        UserResponse response = new UserResponse(userId, user.getUserName(), user.getEmail(), user.getName(), user.getRole(), user.getTeam(), user.getCreatedAt());
        // when
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userMapper.userResponse(user)).thenReturn(response);

        UserResponse result = externalUserService.getMe(userId);

        // then
        Assertions.assertThat(result).isEqualTo(response); //결과와 예상한 응답이 같은지
    }

    @Test
    void getMe_요청시_유저를찾지못한다() {
        // given
        Long userId = 1L;
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> externalUserService.getMe(userId))
                .isInstanceOf(UserException.class)
                .extracting("errorCode")
                .isEqualTo(UserErrorCode.USER_NOT_FOUND);
    }
}

