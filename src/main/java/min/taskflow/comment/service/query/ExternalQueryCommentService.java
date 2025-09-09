package min.taskflow.comment.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.task.service.query.InternalQueryTaskService;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalQueryCommentService {

    private final CommentRepository commentRepository;
    private final InternalQueryTaskService taskService;
    private final InternalQueryUserService userService;
    private final CommentMapper commentMapper;

    public Page<CommentResponse> getComments(Long taskId, Pageable pageable, String sort) {

        taskService.validateTaskExists(taskId);

        Sort.Direction orderBy = sort.equals("oldest") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortByCreatedAt = Sort.by(orderBy, "createdAt");
        Pageable parentPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByCreatedAt);

        // 1. 부모 댓글 페이징 조회
        Page<Comment> parentComments = commentRepository.findParentComments(taskId, parentPageable);
        List<CommentResponse> allComments = new ArrayList<>();

        for (Comment parentComment : parentComments.getContent()) {
            UserResponse parentUserResponse = userService.toUserResponse(parentComment.getUser());
            CommentResponse parentResponse = commentMapper.toCommentResponse(parentComment, parentUserResponse);

            // 2. 대댓글 조회
            List<Comment> childComments = commentRepository.findChildComments(parentComment.getCommentId(), sortByCreatedAt);
            List<CommentResponse> replies = childComments.stream()
                    .map(childComment -> {
                        UserResponse childUserResponse = userService.toUserResponse(childComment.getUser());
                        return commentMapper.toCommentResponse(childComment, childUserResponse);
                    })
                    .toList();

            allComments.add(parentResponse);
            allComments.addAll(replies);
        }

        PageImpl<CommentResponse> comments = new PageImpl<>(allComments, pageable, parentComments.getTotalElements());

        return comments;
    }
}
