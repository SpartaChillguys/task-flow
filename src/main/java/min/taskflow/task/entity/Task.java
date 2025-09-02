package min.taskflow.task.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Task {

    @Id
    private Long taskId;

}
