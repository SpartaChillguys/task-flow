package min.taskflow.user;


import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.repository.UserRepository;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InternalQueryUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    InternalQueryUserService internalQueryUserService;


    @Test
    void findByTeamIsNull_조회시_팀이null인유저들조회() {
        // given
        User user1 = User.builder()
                .userName("user1")
                .password("password1")
                .email("user1@email.com")
                .name("홍길동")
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .userName("user2")
                .password("password2")
                .email("user2@email.com")
                .name("김철수")
                .role(UserRole.USER)
                .build();

        when(userRepository.findByTeamIsNull()).thenReturn(List.of(user1, user2));

        // when
        List<UserResponse> result = internalQueryUserService.findByTeamIsNull();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllUsers_전체유저조회시_UserResponse리스트반환() {
        // given
        User user1 = User.builder()
                .userName("user1")
                .password("password1")
                .email("user1@email.com")
                .name("홍길동")
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .userName("user2")
                .password("password2")
                .email("user2@email.com")
                .name("김철수")
                .role(UserRole.USER)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // when
        List<UserResponse> result = internalQueryUserService.findAllUsersAsResponse();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllUserNames_전체유저이름조회시_이름리스트반환() {
        // given
        User user1 = User.builder()
                .userName("user1")
                .password("password1")
                .email("user1@email.com")
                .name("홍길동")
                .role(UserRole.USER)
                .build();

        User user2 = User.builder()
                .userName("user2")
                .password("password2")
                .email("user2@email.com")
                .name("김철수")
                .role(UserRole.USER)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // when
        List<String> result = internalQueryUserService.findAllUserNames();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder("홍길동", "김철수");
    }


}
