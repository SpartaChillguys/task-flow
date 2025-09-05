package min.taskflow.comment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentPageResponse;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentErrorCode;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.InternalTaskService;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.service.InternalUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final InternalTaskService internalTaskService;
    private final InternalUserService internalUserService;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse createComment(Long taskId,
                                         @Valid CommentRequest request,
                                         Long userId) {

        Task task = internalTaskService.findByTaskId(taskId);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

            if (!parentComment.getTask().getTaskId().equals(task.getTaskId())) {
                throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
            }
        }

        Comment comment = commentMapper.toEntity(request, task, internalUserService.findByUserId(userId));
        Comment savedComment = commentRepository.save(comment);
        UserResponse userResponse = internalUserService.toUserResponse(savedComment.getUser());

        return commentMapper.toCommentResponse(savedComment, userResponse);
    }

    @Transactional
    public CommentPageResponse getComments(Long taskId, Pageable pageable, String sort) {

        internalTaskService.findByTaskId(taskId);

        Sort.Direction orderBy = sort.equals("oldest") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortByCreatedAt = Sort.by(orderBy, "createdAt");
        Pageable parentPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByCreatedAt);

        // 1. 부모 댓글 페이징 조회
        Page<Comment> parentComments = commentRepository.findByTask_TaskIdAndParentIdIsNull(taskId, parentPageable);
        List<CommentResponse> allComments = new ArrayList<>();

        for (Comment parentComment : parentComments.getContent()) {
            UserResponse parentUserResponse = internalUserService.toUserResponse(parentComment.getUser());
            CommentResponse parentResponse = commentMapper.toCommentResponse(parentComment, parentUserResponse);

            // 2. 대댓글 조회
            List<Comment> childComments = commentRepository.findByParentId(parentComment.getCommentId(), sortByCreatedAt);
            List<CommentResponse> replies = childComments.stream()
                    .map(childComment -> {
                        UserResponse childUserResponse = internalUserService.toUserResponse(childComment.getUser());
                        return commentMapper.toCommentResponse(childComment, childUserResponse);
                    })
                    .toList();

            allComments.add(parentResponse);
            allComments.addAll(replies);
        }

        return commentMapper.toCommentPageResponse(allComments, parentComments);
    }
}
