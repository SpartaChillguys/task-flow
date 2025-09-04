package min.taskflow.team.dto;

import min.taskflow.user.entity.User;

public record TeamMemberResponse(
        Long userId,
        String username,
        String email,
        String name
) {}