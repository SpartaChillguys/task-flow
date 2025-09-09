package min.taskflow.fixture;

import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Status;
import min.taskflow.task.entity.Task;
import min.taskflow.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class TaskFixture {

    public static Task createTask(User assignee) {

        Task task = Task.builder()
                .title("작업 보드 구현")
                .description("드래그 앤 드롭 기능이 있는 작업 보드 컴포넌트를 만듭니다")
                .status(Status.TODO)
                .priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.of(2025, 9, 9, 12, 30))
                .assigneeId(assignee.getUserId())
                .build();
        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.of(2025, 9, 5, 15, 30));
        ReflectionTestUtils.setField(task, "updatedAt", LocalDateTime.of(2025, 9, 5, 15, 30));

        return task;
    }

    public static Task createTaskWithId(User user, Long taskId) {

        Task task = createTask(user);
        ReflectionTestUtils.setField(task, "taskId", taskId);

        return task;
    }
}
