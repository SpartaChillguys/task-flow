package min.taskflow.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.service.CommentService;
import min.taskflow.common.annotation.Auth;
import min.taskflow.common.response.ApiResponse;
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
                                                                      @Auth Long userId) {

        CommentResponse comment = commentService.createComment(taskId, request, userId);

        return ApiResponse.success(comment, "댓글이 생성되었습니다.");
    }
}
