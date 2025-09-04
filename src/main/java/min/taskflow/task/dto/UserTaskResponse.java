package min.taskflow.task.dto;

import lombok.Builder;
import lombok.Getter;
import min.taskflow.user.entity.User;

@Getter
@Builder
public class UserTaskResponse {

    private Long userId;
    private String name;
    private String email;

    public static UserTaskResponse from(User u) {
        if (u == null) return null;
        return UserTaskResponse.builder()
                .userId(u.getUserId())
                .name(u.getName())
                .email(u.getEmail())
                .build();
    }
}
