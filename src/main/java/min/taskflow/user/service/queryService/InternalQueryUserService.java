package min.taskflow.user.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*
다른 도메인에서 참조할 메서드들 모음집
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InternalQueryUserService {

    private final UserRepository userRepository;


    public User findByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return user;
    }

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public List<User> findByTeamIsNull() {

        return userRepository.findByTeamIsNull(); //테스트에서 stackoverflow 나서 바로 반환해줘야할것 같습니다.
    }

}
