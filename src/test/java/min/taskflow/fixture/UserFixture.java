package min.taskflow.fixture;

import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.team.entity.Team;

public class UserFixture {

    public static User createUser() {

        return User.builder()
                .userName("chulsoo")
                .password("abcd1234!@#$")
                .email("john@example.com")
                .name("김철수")
                .role(UserRole.USER)
                .team(Team.builder().name("개발팀").description("프론트엔드 및 백엔드 개발자들").build())
                .build();
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
