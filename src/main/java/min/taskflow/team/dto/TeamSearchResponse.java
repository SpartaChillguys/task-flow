package min.taskflow.team.dto;

import lombok.Builder;

@Builder
public record TeamSearchResponse(
        Long id,
        String name,
        String description
) {}