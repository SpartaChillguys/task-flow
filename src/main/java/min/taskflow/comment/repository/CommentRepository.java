package min.taskflow.comment.repository;

import min.taskflow.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "user")
    Page<Comment> findByTask_TaskIdAndParentIdIsNull(Long taskId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    List<Comment> findByParentId(Long parentCommentId, Sort sort);
}
