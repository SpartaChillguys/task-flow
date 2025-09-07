package min.taskflow.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.request.CommentUpdateRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.service.CommentService;
import min.taskflow.common.response.ApiPageResponse;
import min.taskflow.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@PathVariable Long taskId,
                                                                      @Valid @RequestBody CommentRequest request,
                                                                      @AuthenticationPrincipal Long userId) {

        CommentResponse comment = commentService.createComment(taskId, request, userId);

        return ApiResponse.success(comment, "댓글이 생성되었습니다.");
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<CommentResponse>> getComments(@PathVariable Long taskId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "newest") String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentService.getComments(taskId, pageable, sort);

        return ApiPageResponse.success(comments, "댓글 목록을 조회했습니다.");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable Long taskId,
                                                                      @RequestBody CommentUpdateRequest request,
                                                                      @PathVariable Long commentId,
                                                                      @AuthenticationPrincipal Long userId) {

        CommentResponse updatedComment = commentService.updateComment(taskId, request, commentId, userId);

        return ApiResponse.success(updatedComment, "댓글이 수정되었습니다.");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long taskId,
                                                           @PathVariable Long commentId,
                                                           @AuthenticationPrincipal Long userId) {

        int deleteCount = commentService.deleteComment(taskId, commentId, userId);

        String message = (deleteCount > 1)
                ? "댓글과 대댓글들이 삭제되었습니다."
                : "댓글이 삭제되었습니다.";

        return ApiResponse.noContent(message);
    }
}
