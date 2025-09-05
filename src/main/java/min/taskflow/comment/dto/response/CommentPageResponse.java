package min.taskflow.comment.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CommentPageResponse(

        List<CommentResponse> content,
        long totalElements,
        int totalPages,
        int size,
        int number
) {
}
