package min.taskflow.comment;

import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.fixture.CommentFixture;
import min.taskflow.fixture.TaskFixture;
import min.taskflow.fixture.TeamFixture;
import min.taskflow.fixture.UserFixture;
import min.taskflow.task.entity.Task;
import min.taskflow.team.entity.Team;
import min.taskflow.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByTask_TaskIdAndParentIdIsNull_부모_댓글_페이징_조회와_최신순_정렬() {

        // given
        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        em.persist(team);
        em.persist(user);
        em.persist(task);

        for (int i = 0; i < 15; i++) {
            Comment comment = CommentFixture.createComment((i + 1) + ". 댓글 내용", task, user);
            commentRepository.save(comment);
        }

        em.flush();
        em.clear();

        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentRepository.findByTask_TaskIdAndParentIdIsNull(task.getTaskId(), pageable);

        // then
        assertThat(comments.getContent()).hasSize(10);
        assertThat(comments.getTotalElements()).isEqualTo(15);
        assertThat(comments.getTotalPages()).isEqualTo(2);
        assertThat(comments.getContent().get(0).getContent()).isEqualTo("15. 댓글 내용");
    }

    @Test
    void findByParentId_자식_댓글_조회_오래된순_정렬() {

        // given
        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        em.persist(team);
        em.persist(user);
        em.persist(task);

        Comment parentComment = CommentFixture.createComment("부모 댓글", task, user);
        commentRepository.save(parentComment);

        for (int i = 0; i < 5; i++) {
            Comment childComment = CommentFixture.createComment((i + 1) + ". 자식 댓글", task, user);
            ReflectionTestUtils.setField(childComment, "parentId", parentComment.getCommentId());
            commentRepository.save(childComment);
        }

        // when
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<Comment> childComments = commentRepository.findByParentId(parentComment.getCommentId(), sort);

        // then
        assertThat(childComments).hasSize(5);
        assertThat(childComments.get(0).getContent()).isEqualTo("1. 자식 댓글");
    }
}
