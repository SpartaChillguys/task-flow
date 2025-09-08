package min.taskflow.task.repository;

import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task save(Task task);

    Optional<Task> findByTaskId(Long taskId);

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByTitleContaining(String query, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    List<Task> findByAssigneeIdAndDueDateBetween(Long assigneeId, LocalDateTime dueDateAfter, LocalDateTime dueDateBefore);

    List<Task> findByAssigneeIdAndDueDateAfter(Long assigneeId, LocalDateTime dueDateAfter);

    List<Task> findByAssigneeIdAndDueDateBefore(Long assigneeId, LocalDateTime dueDateBefore);

    Long countByAssigneeIdInAndStatus(List<Long> userIds, Status status);

    Long countByAssigneeIdAndDueDateBefore(Long assigneeId, LocalDateTime dueDateBefore);

    Long countByAssigneeIdAndDueDateBetween(Long assigneeId, LocalDateTime dueDateAfter, LocalDateTime dueDateBefore);

    // 수영 : 제목에 검색어가 포함된 Task 조회 (대소문자 무시)
    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByAssigneeIdIn(List<Long> memberIdsByTeamId);

    Long countTasksByAssigneeIdAndDueDateBetween(Long assigneeId, LocalDateTime dueDateAfter, LocalDateTime dueDateBefore);

    Long countTasksByAssigneeIdAndDueDateBetweenAndStatus(Long loginUserId, LocalDateTime startOfDay, LocalDateTime endOfDay, Status status);
}
