package min.taskflow.user.service.command;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InternalCommandUserService {

    private final UserRepository userRepository;

    public void saveUserInfo(User user) {

        User targetUser = userRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        targetUser.assignTeam(user.getTeam());
    }

}
