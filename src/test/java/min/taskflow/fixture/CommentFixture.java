package min.taskflow.fixture;

import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.task.entity.Task;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class CommentFixture {

    public static Comment createComment(String content, Task task, User user) {

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .user(user)
                .build();
        ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.of(2025, 9, 5, 15, 30));
        ReflectionTestUtils.setField(comment, "updatedAt", LocalDateTime.of(2025, 9, 5, 15, 30));

        return comment;
    }

    public static CommentResponse createCommentResponse(Long commentId, String content, Long taskId, Long userId, UserResponse userResponse) {

        return CommentResponse.builder()
                .commentId(commentId)
                .content(content)
                .taskId(taskId)
                .userId(userId)
                .user(userResponse)
                .createdAt(LocalDateTime.of(2025, 9, 4, 18, 30))
                .updatedAt(LocalDateTime.of(2025, 9, 4, 18, 30))
                .build();
    }
}
