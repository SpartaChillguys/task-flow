package min.taskflow.log.Repository;

import min.taskflow.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<Log, Long> {
}
