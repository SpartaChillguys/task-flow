package min.taskflow.auth.dto.info;

import min.taskflow.user.enums.UserRole;

public record UserInfo(Long userId, UserRole userRole) {
}
