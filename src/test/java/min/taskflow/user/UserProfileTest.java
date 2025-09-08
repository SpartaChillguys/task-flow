package min.taskflow.user;

import min.taskflow.user.dto.response.UserProfileResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import min.taskflow.user.service.query.ExternalQueryUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    ExternalQueryUserService externalQueryUserService;

    @Test
    void getMe_요청시_성공적() {
        // given

        User user = User.builder()
                .userName("user1")
                .password("password1")
                .email("user1@email.com")
                .name("홍길동")
                .role(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(user, "userId", 1L);
        UserProfileResponse response = userMapper.toProfileResponse(user);

        // when
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(userMapper.toProfileResponse(user)).thenReturn(response);

        UserProfileResponse result = externalQueryUserService.getMe(user.getUserId());

        // then
        Assertions.assertThat(result).isEqualTo(response); //결과와 예상한 응답이 같은지
    }

    @Test
    void getMe_요청시_유저를찾지못한다() {
        // given
        Long userId = 1L;
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> externalQueryUserService.getMe(userId))
                .isInstanceOf(UserException.class)
                .extracting("errorCode")
                .isEqualTo(UserErrorCode.USER_NOT_FOUND);
    }
}

