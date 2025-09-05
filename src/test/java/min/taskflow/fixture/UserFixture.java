package min.taskflow.fixture;

import min.taskflow.team.entity.Team;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class UserFixture {

    public static User createUser(Team team) {

        User user = User.builder()
                .userName("chulsoo")
                .password("abcd1234!@#$")
                .email("john@example.com")
                .name("김철수")
                .role(UserRole.USER)
                .team(team)
                .build();
        ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.of(2025, 9, 5, 15, 30));
        ReflectionTestUtils.setField(user, "updatedAt", LocalDateTime.of(2025, 9, 5, 15, 30));

        return user;
    }

    public static User createUserWithId(Team team, Long userId) {

        User user = createUser(team);
        ReflectionTestUtils.setField(user, "userId", userId);

        return user;
    }

    public static UserResponse createUserResponse(User user, Long userId) {

        return UserResponse.builder()
                .userId(userId)
                .userName(user.getUserName())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .team(user.getTeam())
                .build();
    }
}
