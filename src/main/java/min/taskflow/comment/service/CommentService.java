package min.taskflow.comment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentErrorCode;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.InternalTaskService;
import min.taskflow.user.service.InternalUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final InternalTaskService internalTaskService;
    private final InternalUserService internalUserService;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse createComment(Long taskId, @Valid CommentRequest request, Long userId) {

        Task task = internalTaskService.findByTaskId(taskId);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

            if (!parentComment.getTask().getTaskId().equals(task.getTaskId())) {
                throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
            }
        }

        Comment comment = Comment.builder()
                .content(request.content())
                .task(task)
                .user(internalUserService.findByUserId(userId))
                .parentId(request.parentId())
                .build();
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);
    }
}
