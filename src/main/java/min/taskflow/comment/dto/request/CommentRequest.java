package min.taskflow.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(

        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(max = 255, message = "댓글은 255자까지 작성 가능합니다.")
        String content,

        Long parentId
) {}
