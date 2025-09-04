package min.taskflow.task.taskRepository;

import min.taskflow.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task save(Task task);

    //쿼리문으로 검색 수행
    @Query("SELECT t FROM Task t " +
            "WHERE t.title LIKE %:keyword% " +
            "OR t.description LIKE %:keyword%")
    List<Task> findByKeyword(String keyWord);

    Optional<Task> findById(Long taskId);

}
