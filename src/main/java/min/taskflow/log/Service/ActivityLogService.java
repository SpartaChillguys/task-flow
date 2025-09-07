package min.taskflow.log.Service;

import lombok.RequiredArgsConstructor;
import min.taskflow.log.ActivityType;
import min.taskflow.log.Repository.ActivityLogRepository;
import min.taskflow.log.entity.Log;
import min.taskflow.log.mapper.LogMapper;
import min.taskflow.log.response.ActivityLogResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository logRepository;
    private final InternalQueryUserService internalQueryUserService;
    private final LogMapper logMapper;

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


    public Page<ActivityLogResponse> getLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));

        return logRepository.findAll(pageable)
                .map(log -> {
                    // 지금은 userId 따로 없으니 userName 기준으로 조회
                    UserProfileResponse userProfile = internalQueryUserService.findByName(log.getUserName());
                    return logMapper.toActivityLogResponse(log, userProfile);
                });
    }
}
