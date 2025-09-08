package min.taskflow.comment.service.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.request.CommentUpdateRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentErrorCode;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.common.annotation.ActivityLogger;
import min.taskflow.log.ActivityType;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.query.InternalQueryTaskService;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalCommandCommentService {

    private final CommentRepository commentRepository;
    private final InternalQueryTaskService taskService;
    private final InternalQueryUserService userService;
    private final CommentMapper commentMapper;

    @ActivityLogger(type = ActivityType.COMMENT_CREATED)
    @Transactional
    public CommentResponse createComment(Long taskId,
                                         @Valid CommentRequest request,
                                         Long userId) {

        Task task = taskService.getTaskByTaskId(taskId);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

            if (!parentComment.getTask().getTaskId().equals(task.getTaskId())) {
                throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
            }
        }

        Comment comment = commentMapper.toEntity(request, task, userService.getUserByUserId(userId));
        Comment savedComment = commentRepository.save(comment);
        UserResponse userResponse = userService.toUserResponse(savedComment.getUser());

        return commentMapper.toCommentResponse(savedComment, userResponse);
    }

    @ActivityLogger(type = ActivityType.COMMENT_UPDATED)
    @Transactional
    public CommentResponse updateComment(Long taskId, CommentUpdateRequest request, Long commentId, Long userId) {

        Comment comment = validateCommentAccess(taskId, commentId, userId);

        comment.updateContent(request.content());
        UserResponse userResponse = userService.toUserResponse(comment.getUser());

        return commentMapper.toCommentResponse(comment, userResponse);
    }

    @ActivityLogger(type = ActivityType.COMMENT_DELETED)
    @Transactional
    public int deleteComment(Long taskId, Long commentId, Long userId) {

        Comment comment = validateCommentAccess(taskId, commentId, userId);

        int deleteCount = commentRepository.softDeleteCommentAndReplies(comment.getCommentId());

        return deleteCount;
    }

    // 댓글이 해당 작업에 속하고 작성자가 요청한 사용자와 일치하는지 확인합니다.
    private Comment validateCommentAccess(Long taskId, Long commentId, Long userId) {

        taskService.validateTaskExists(taskId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (comment.isDeleted()) {
            throw new CommentException(CommentErrorCode.COMMENT_ALREADY_DELETED);
        }

        if (!comment.getTask().getTaskId().equals(taskId)) {
            throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
        }

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CommentException(CommentErrorCode.COMMENT_FORBIDDEN);
        }

        return comment;
    }
}
