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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Page<ActivityLogResponse> getLogs(
            int page,
            int size,
            ActivityType type,
            Long userId,
            Long taskId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));

        Specification<Log> spec = (root, query, cb) -> cb.conjunction();

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
        }
        if (taskId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("taskId"), taskId));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.between(
                    root.get("timeStamp"),
                    startDate.atStartOfDay(),
                    endDate.atTime(23, 59, 59)
            ));
        }

        Page<Log> logs = logRepository.findAll(spec, pageable);

        return logs.map(log -> {
            UserProfileResponse userProfile = internalQueryUserService.findByName(log.getUserName());
            return logMapper.toActivityLogResponse(log, userProfile);
        });
    }


//    public Page<ActivityLogResponse> getLogs(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));
//
//        return logRepository.findAll(pageable)
//                .map(log -> {
//                    // 지금은 userId 따로 없으니 userName 기준으로 조회
//                    UserProfileResponse userProfile = internalQueryUserService.findByName(log.getUserName());
//                    return logMapper.toActivityLogResponse(log, userProfile);
//                });
//    }
}
