package min.taskflow.comment.mapper;


import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.task.entity.Task;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequest request, Task task, User user) {

        return Comment.builder()
                .content(request.content())
                .task(task)
                .user(user)
                .parentId(request.parentId())
                .build();
    }

    public CommentResponse toCommentResponse(Comment comment, UserResponse user) {

        return CommentResponse.builder()
                .id(comment.getCommentId())
                .content(comment.getContent())
                .taskId(comment.getTask().getTaskId())
                .userId(comment.getUser().getUserId())
                .user(user)
                .parentId(comment.getParentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
