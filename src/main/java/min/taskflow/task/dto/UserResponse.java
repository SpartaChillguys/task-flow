package min.taskflow.task.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private String email;

}
