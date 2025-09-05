package min.taskflow.comment.dto.response;

import lombok.Builder;
import min.taskflow.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(

        Long commentId,
        String content,
        Long taskId,
        Long userId,
        UserResponse user,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
