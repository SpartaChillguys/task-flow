package min.taskflow.log.Service;

import lombok.RequiredArgsConstructor;
import min.taskflow.log.ActivityType;
import min.taskflow.log.Repository.ActivityLogRepository;
import min.taskflow.log.entity.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository logRepository;

    public void saveLog(Long taskId, String userName, ActivityType type, String description) {
        Log log = new Log(
                taskId,
                userName,
                type,
                description,
                LocalDateTime.now()
        );
        logRepository.save(log);
    }
}
