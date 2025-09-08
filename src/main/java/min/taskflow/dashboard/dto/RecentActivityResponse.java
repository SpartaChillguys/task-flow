package min.taskflow.dashboard.dto;

import lombok.Builder;
import min.taskflow.log.ActivityType;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;

import java.time.LocalDateTime;

@Builder
public record RecentActivityResponse(Long id,
                                     Long userId,
                                     AssigneeSummaryResponse user,
                                     ActivityType action,
                                     TargetDomainType targetType,
                                     Long targetId,
                                     String description,
                                     LocalDateTime createdAt) {
}
