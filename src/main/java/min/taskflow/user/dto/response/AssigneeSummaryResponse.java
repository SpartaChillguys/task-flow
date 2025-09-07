package min.taskflow.user.dto.response;

import lombok.Builder;

@Builder
public record AssigneeSummaryResponse(
        Long userId,
        String name
) {
}
