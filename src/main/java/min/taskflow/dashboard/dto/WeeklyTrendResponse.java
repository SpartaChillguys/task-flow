package min.taskflow.dashboard.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WeeklyTrendResponse(String name,
                                  Long tasks,
                                  Long completed,
                                  LocalDate date) {
}
