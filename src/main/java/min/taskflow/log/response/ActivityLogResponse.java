package min.taskflow.log.response;

import lombok.Builder;
import min.taskflow.log.ActivityType;
import min.taskflow.user.dto.response.UserProfileResponse;

import java.time.LocalDateTime;

@Builder
public record ActivityLogResponse(
        Long id,
        ActivityType type,
        Long userId,
        UserProfileResponse user,
        Long taskId,
        LocalDateTime timestamp,
        String description
) {

}
