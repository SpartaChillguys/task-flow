package min.taskflow.team.dto;

import lombok.Builder;

@Builder
public record TeamSearchResponse(
        Long teamId,
        String name,
        String description
) {
}