package min.taskflow.auth.dto;

import lombok.Builder;
import min.taskflow.user.enums.UserRole;

import java.time.LocalDateTime;

/*
회원가입시 response DTO
 */
@Builder
public record UserSaveResponse(Long id,
                               String username,
                               String email,
                               String name,
                               UserRole role,
                               LocalDateTime createdAt) {

}
