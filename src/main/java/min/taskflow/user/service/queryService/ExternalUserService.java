package min.taskflow.user.service.queryService;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
유저 관련 서비스(프로필 조회)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExternalUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //프로필 조회 로직

    public UserResponse getMe(Long id) {

        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = userMapper.userResponse(user);

        return userResponse;
    }
}
