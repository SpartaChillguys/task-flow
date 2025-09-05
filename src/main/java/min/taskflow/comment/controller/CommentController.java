package min.taskflow.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentPageResponse;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.service.CommentService;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@PathVariable Long taskId,
                                                                      @Valid @RequestBody CommentRequest request,
                                                                      @Auth User user) {

        CommentResponse comment = commentService.createComment(taskId, request, user.getUserId());

        return ApiResponse.success(comment, "댓글이 생성되었습니다.");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CommentPageResponse>> getComments(@PathVariable Long taskId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "newest") String sort) {

        Pageable pageable = PageRequest.of(page, size);
        CommentPageResponse comments = commentService.getComments(taskId, pageable, sort);

        return ApiResponse.success(comments, "댓글 목록을 조회했습니다.");
    }
}
