package min.taskflow.log.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import min.taskflow.common.annotation.ActivityLogger;
import min.taskflow.log.ActivityType;
import min.taskflow.log.Service.ActivityLogService;
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

    @AfterReturning("@annotation(min.taskflow.common.annotation.ActivityLogger)")
    public void logActivity(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // @Loggable 읽기
        ActivityLogger loggable = method.getAnnotation(ActivityLogger.class);
        ActivityType type = loggable.type();

        // 인자 꺼내기
        Object[] args = joinPoint.getArgs();
        Long taskId = null;
        Long userId = null;
        String userName = "unknown";

//        if (args.length > 0 && args[0] instanceof Long) {
//            taskId = (Long) args[0];
//        }
//        if (args.length > 2 && args[2] instanceof Long) {
//            userId = (Long) args[2];
//            // TODO: userService 통해 userName 조회 가능
//        }

        logService.saveLog(taskId, userName, type, type.name() + " 발생");
        log.info("✅ 로그 저장됨: type={}, taskId={}, userId={}", type, taskId, userId);
    }
}
