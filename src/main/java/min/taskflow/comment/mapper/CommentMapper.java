package min.taskflow.comment.mapper;

import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.service.InternalUserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final InternalUserService internalUserService;

    public CommentResponse toResponse(Comment comment) {

        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .taskId(comment.getTask().getTaskId())
                .userId(comment.getUser().getUserId())
                .user(internalUserService.toUserResponse(comment.getUser()))
                .parentId(comment.getParentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
