package min.taskflow.log.Repository;

import min.taskflow.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityLogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {
}
