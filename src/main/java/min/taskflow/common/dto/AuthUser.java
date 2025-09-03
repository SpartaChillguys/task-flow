package min.taskflow.common.dto;

import lombok.Getter;
import min.taskflow.user.enums.UserRole;

@Getter
public class AuthUser {
    private final Long id;

    private final UserRole userRole;

    public AuthUser(Long id, UserRole userRole) {
        this.id = id;
        this.userRole = userRole;
    }
}
