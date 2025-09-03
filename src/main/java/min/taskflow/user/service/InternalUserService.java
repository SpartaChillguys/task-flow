package min.taskflow.user.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static min.taskflow.user.exception.UserErrorCode.USER_NOT_FOUND;

/*
다른 도메인에서 참조할 메서드들 모음집
 */
@Service
@RequiredArgsConstructor
public class InternalUserService {

    private final UserRepository userRepository;


    public void findByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
