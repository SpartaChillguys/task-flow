package min.taskflow.team.repository;

import min.taskflow.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    // 팀 단건 조회 + 멤버 포함
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.members WHERE t.teamId = :teamId AND t.deleted = false")
    Optional<Team> findByIdWithMembers(@Param("teamId") Long teamId);

    // 팀 전체 조회 + 멤버 포함
    @Query("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.members WHERE t.deleted = false")
    List<Team> findAllWithMembers();
}
