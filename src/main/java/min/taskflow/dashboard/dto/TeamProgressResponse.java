package min.taskflow.dashboard.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record TeamProgressResponse(Map<String, Long> teamProgress) {
}
