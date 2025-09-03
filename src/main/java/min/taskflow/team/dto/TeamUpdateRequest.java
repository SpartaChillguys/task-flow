package min.taskflow.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {

    @NotBlank(message = "팀 이름을 입력해주세요.")
    @Size(max = 255, message = "팀 이름은 255자를 넘을 수 없습니다.")
    private String name;

    @Size(max = 500, message = "설명은 500자를 넘을 수 없습니다.")
    private String description;
}
