package min.taskflow.user.repository;

import min.taskflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);  //중복 검증을 위함

    boolean existsByEmail(String email); //중복 검증을 위함

    //Optional<User> findByEmail(String mail);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(Long userId);

    List<User> findByTeamIsNull();

    List<User> findByNameContaining(String Name);

    // 수영 : 이름 또는 username에 검색어가 포함된 User 조회 (대소문자 무시)
    List<User> findByNameContainingIgnoreCaseOrUserNameContainingIgnoreCase(String name, String userName);
}
