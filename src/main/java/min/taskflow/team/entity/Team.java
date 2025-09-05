package min.taskflow.team.entity;

import jakarta.persistence.*;
import lombok.*;
import min.taskflow.common.entity.BaseEntity;
import min.taskflow.user.entity.User;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "team", orphanRemoval = false)
    private List<User> members = new ArrayList<>();

    @Builder
    private Team(String name, String description, List<User> members) {
        this.name = name;
        this.description = description;
        if (members != null) {
            this.members = members;
        }
    }

    // 팀 정보 수정
    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 팀 멤버 추가
    public void addMember(User member) {
        if (!members.contains(member)) {
            members.add(member);
            member.assignTeam(this); // setter 대신 편의 메서드 사용
        }
    }

    // 팀 멤버 삭제
    public void removeMember(User member) {
        if (members.remove(member)) {
            member.removeFromTeam();
        }
    }

    // 팀 삭제 시 멤버와 관계 끊기
    public void delete() {
        super.delete();
        for (User member : new ArrayList<>(members)) {
            removeMember(member);
        }
    }
}
