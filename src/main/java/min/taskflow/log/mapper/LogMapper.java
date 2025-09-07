package min.taskflow.log.mapper;


import min.taskflow.log.entity.Log;
import min.taskflow.log.response.ActivityLogResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {

    public ActivityLogResponse toActivityLogResponse(Log log, UserProfileResponse userProfile) {

        return ActivityLogResponse.builder()
                .id(log.getLogId())
                .type(log.getType())
                .userId(userProfile.id())
                .user(userProfile)
                .taskId(log.getTaskId())
                .timestamp(log.getTimeStamp())
                .description(log.getDescription())
                .build();


    }
}
