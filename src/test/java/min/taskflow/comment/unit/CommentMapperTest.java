package min.taskflow.comment.unit;

import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.fixture.CommentFixture;
import min.taskflow.fixture.TaskFixture;
import min.taskflow.fixture.TeamFixture;
import min.taskflow.fixture.UserFixture;
import min.taskflow.task.entity.Task;
import min.taskflow.team.entity.Team;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void toEntity_댓글_요청을_엔티티로_변환() {

        // given
        CommentRequest request = new CommentRequest("댓글 내용", null);

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);

        // when
        Comment comment = commentMapper.toEntity(request, task, user);

        // then
        assertEquals("댓글 내용", comment.getContent());
        assertEquals(task.getTaskId(), comment.getTask().getTaskId());
        assertEquals(user.getUserId(), comment.getUser().getUserId());
        assertNull(comment.getParentId());
    }

    @Test
    void toCommentResponse_엔티티를_응답_객체로_변환() {

        // given
        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUserWithId(team, 1L);
        Task task = TaskFixture.createTask(user);
        Comment comment = CommentFixture.createCommentWithId("댓글 내용", task, user, 2L);
        UserResponse userResponse = UserFixture.createUserResponse(user, user.getUserId());

        // when
        CommentResponse response = commentMapper.toCommentResponse(comment, userResponse);

        // then
        assertEquals(comment.getCommentId(), response.commentId());
        assertEquals("댓글 내용", response.content());
        assertEquals(user.getUserId(), response.userId());
        assertEquals(userResponse, response.user());
    }
}