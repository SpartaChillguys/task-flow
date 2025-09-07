package min.taskflow.log.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import min.taskflow.common.annotation.ActivityLogger;
import min.taskflow.log.ActivityType;
import min.taskflow.log.Service.ActivityLogService;
import min.taskflow.task.dto.response.task.TaskResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogService logService;
    private final InternalQueryUserService internalQueryUserService;

    @AfterReturning(value =
            "@annotation(min.taskflow.common.annotation.ActivityLogger)",
            returning = "result" //TaskResponse를 통해서 값을 받아오기 위함
    )
    public void logActivity(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // @Loggable 읽기
        ActivityLogger loggable = method.getAnnotation(ActivityLogger.class);
        ActivityType type = loggable.type();

        Long taskId = null;
        Long userId = null;
        String userName = "unknown";

        // 반환값에서 taskId, assigneeId 꺼내기
        if (result instanceof TaskResponse taskResponse) {
            taskId = taskResponse.id();
            userId = taskResponse.assigneeId();

            if (userId != null) {
                userName = internalQueryUserService.getUserNameByUserId(userId);
            }
        }

        logService.saveLog(taskId, userName, type, type.name() + " 발생");
        log.info("✅ 로그 저장됨: type={}, taskId={}, userId={}", type, taskId, userId);
    }
}
