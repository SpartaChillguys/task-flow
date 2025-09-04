package min.taskflow.task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;  // ENUM: TODO, IN_PROGRESS, COMPLETED
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_user_id")
    private User assignee;

    public Task(User assignee,
                String description,
                LocalDateTime dueDate,
                Priority priority,
                TaskStatus status,
                String title
    ) {
        this.assignee = assignee;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.title = title;
    }

    @Override
    public void delete() {
        super.delete();
    }
}
