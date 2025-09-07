package min.taskflow.task.service;

import min.taskflow.common.annotation.ActivityLogger;
import min.taskflow.log.ActivityType;
import org.springframework.stereotype.Service;

@Service
public class TaskDummyService {

    @ActivityLogger(type = ActivityType.TASK_CREATED)
    public void dummyTaskCreate(String userName) {
        System.out.println("👉 Task 생성 로직 실행됨 (userName=" + userName + ")");
    }
}
