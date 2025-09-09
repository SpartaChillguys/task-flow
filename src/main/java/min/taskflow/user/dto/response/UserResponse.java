package min.taskflow.user.dto.response;

import lombok.Builder;
import min.taskflow.team.entity.Team;
import min.taskflow.user.enums.UserRole;

import java.time.LocalDateTime;

@Builder
public record UserResponse(Long id,
                           String username,
                           String email,
                           String name,
                           UserRole role,
                           Team team,
                           LocalDateTime createdAt) {
}
