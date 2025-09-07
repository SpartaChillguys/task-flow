package min.taskflow.comment.repository;

import min.taskflow.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "user")
    @Query("SELECT c FROM Comment c WHERE c.task.taskId = :taskId AND c.parentId IS NULL")
    Page<Comment> findParentComments(@Param("taskId") Long taskId, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT c FROM Comment c WHERE c.parentId = :parentId")
    List<Comment> findChildComments(@Param("parentId") Long parentId, Sort sort);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.commentId = :commentId OR c.parentId = :commentId")
    int softDeleteCommentAndReplies(@Param("commentId") Long commentId);
}
