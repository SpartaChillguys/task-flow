package min.taskflow.user;


import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.repository.UserRepository;
import min.taskflow.user.service.queryService.InternalQueryUserService;
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
        User user1 = new User("user1", "password1", "user1@email.com",
                "홍길동", UserRole.USER, null);
        User user2 = new User("user2", "password2", "user2@email.com",
                "김철수", UserRole.USER, null);

        when(userRepository.findByTeamIsNull()).thenReturn(List.of(user1, user2));

        // when
        List<User> result = internalQueryUserService.findByTeamIsNull();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(User::getUserName)
                .containsExactlyInAnyOrder("user1", "user2");
    }
}
