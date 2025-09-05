package min.taskflow.fixture;

import min.taskflow.task.entity.Priority;
import min.taskflow.task.entity.Task;
import min.taskflow.task.entity.TaskStatus;
import min.taskflow.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class TaskFixture {

    public static Task createTask(User assignee) {

        return Task.builder()
                .title("작업 보드 구현")
                .description("드래그 앤 드롭 기능이 있는 작업 보드 컴포넌트를 만듭니다")
                .status(TaskStatus.TODO)
                .priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.of(2025, 9, 9, 12, 30))
                .assignee(assignee)
                .build();
    }
}
