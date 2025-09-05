package min.taskflow.team.dto;

public record TeamMemberResponse(
        Long userId,
        String username,
        String email,
        String name
) {}