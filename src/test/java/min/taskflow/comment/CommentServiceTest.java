package min.taskflow.comment;

import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.comment.service.CommentService;
import min.taskflow.fixture.CommentFixture;
import min.taskflow.fixture.TaskFixture;
import min.taskflow.fixture.UserFixture;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.InternalTaskService;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.queryService.InternalUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private InternalTaskService internalTaskService;

    @Mock
    private InternalUserService internalUserService;

    @Test
    void createComment_댓글_생성_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        CommentRequest request = new CommentRequest("댓글 내용", null);

        User user = UserFixture.createUser();
        Task task = TaskFixture.createTask(user);
        Comment comment = CommentFixture.createComment(request.content(), task, user);
        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse commentResponse = CommentFixture
                .createCommentResponse(100L, "댓글 내용", taskId, userId, userResponse);

        when(internalTaskService.findByTaskId(taskId)).thenReturn(task);
        when(internalUserService.findByUserId(userId)).thenReturn(user);
        when(commentMapper.toEntity(request, task, user)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(internalUserService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(any(Comment.class), eq(userResponse))).thenReturn(commentResponse);

        // when
        CommentResponse response = commentService.createComment(taskId, request, userId);

        // then
        assertNotNull(response);
        assertEquals("댓글 내용", response.content());
        assertEquals("김철수", response.user().name());
    }

    @Test
    void createComment_부모_댓글이_존재하지_않는_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("댓글 내용", parentId);

        when(commentRepository.findById(parentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentException.class, () -> commentService.createComment(taskId, request, userId));
    }

    @Test
    void createComment_부모_댓글이_존재하지만_다른_작업에_속한_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("대댓글 내용", parentId);

        User user = UserFixture.createUser();
        Task task = TaskFixture.createTask(user);
        Task otherTask = TaskFixture.createTask(user);
        ReflectionTestUtils.setField(otherTask, "taskId", 20L);
        Comment parentComment = CommentFixture.createComment(request.content(), otherTask, user);

        when(internalTaskService.findByTaskId(taskId)).thenReturn(task);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));

        // when & then
        assertThrows(CommentException.class, () -> commentService.createComment(taskId, request, userId));
    }

    @Test
    void createComment_대댓글_작성_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("대댓글 내용", parentId);

        User user = UserFixture.createUser();
        Task task = TaskFixture.createTask(user);
        ReflectionTestUtils.setField(task, "taskId", 20L);
        Comment parentComment = CommentFixture.createComment("부모 댓글", task, user);
        Comment childComment = CommentFixture.createComment(request.content(), task, user);

        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse commentResponse = CommentFixture
                .createCommentResponse(200L, "대댓글 내용", taskId, userId, userResponse);

        when(internalTaskService.findByTaskId(taskId)).thenReturn(task);
        when(internalUserService.findByUserId(userId)).thenReturn(user);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));
        when(commentMapper.toEntity(request, task, user)).thenReturn(childComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(childComment);
        when(internalUserService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(any(Comment.class), eq(userResponse))).thenReturn(commentResponse);

        // when
        CommentResponse response = commentService.createComment(taskId, request, userId);

        // then
        assertNotNull(response);
        assertEquals("대댓글 내용", response.content());
        assertEquals("김철수", response.user().name());
    }
}
