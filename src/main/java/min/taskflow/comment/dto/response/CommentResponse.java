package min.taskflow.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import min.taskflow.user.dto.response.UserResponse;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponse(

        Long id,
        String content,
        Long taskId,
        Long userId,
        UserResponse user,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
