package min.taskflow.user;


import min.taskflow.common.config.JpaAuditingConfig;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import min.taskflow.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findUsersByNameContaining_김으로검색하면_김포함된이름만조회() {
        // given
        userRepository.save(User.builder()
                .userName("kim123")
                .password("1234")
                .email("kim@example.com")
                .name("김석준")
                .role(UserRole.USER)
                .build());

        userRepository.save(User.builder()
                .userName("lee123")
                .password("1234")
                .email("lee@example.com")
                .name("이민호")
                .role(UserRole.USER)
                .build());

        // when
        List<User> result = userRepository.findByNameContaining("김");

        // then
        assertThat(result).hasSize(1)
                .extracting(User::getName)
                .containsExactly("김석준");
    }

}
