package min.taskflow.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.task.entity.Task;
import min.taskflow.user.entity.User;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long parentId;

    @Builder
    public Comment(String content, Task task, User user, Long parentId) {
        this.content = content;
        this.task = task;
        this.user = user;
        this.parentId = parentId;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
