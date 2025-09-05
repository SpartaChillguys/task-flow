package min.taskflow.fixture;

import min.taskflow.team.entity.Team;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class TeamFixture {

    public static Team createTeam() {

        Team team = Team.builder()
                .name("개발팀")
                .description("프론트엔드 및 백엔드 개발자들")
                .build();
        ReflectionTestUtils.setField(team, "createdAt", LocalDateTime.of(2025, 9, 5, 15, 30));
        ReflectionTestUtils.setField(team, "updatedAt", LocalDateTime.of(2025, 9, 5, 15, 30));

        return team;
    }
}
