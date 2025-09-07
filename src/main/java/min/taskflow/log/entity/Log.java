package min.taskflow.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.taskflow.log.ActivityType;

import java.time.LocalDateTime;

// TODO: 로그 생성일은 BaseEntity 상속 X, 혼자 만들어 쓰기
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private Long taskId;

    private String userName;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String description;

    private LocalDateTime timeStamp;

    public Log(Long taskId, String userName, ActivityType type, String description, LocalDateTime timeStamp) {
        this.taskId = taskId;
        this.userName = userName;
        this.type = type;
        this.description = description;
        this.timeStamp = timeStamp;
    }
}
