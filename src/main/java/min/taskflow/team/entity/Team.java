package min.taskflow.team.entity;

import jakarta.persistence.*;
import lombok.*;
import min.taskflow.common.entity.BaseEntity;

import java.awt.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "team")
public class Team extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
