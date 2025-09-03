package min.taskflow.auth.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.PasswordEncoder;
import min.taskflow.user.dto.UserSaveRequest;
import min.taskflow.user.dto.UserSaveResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 사용자 인증 관련 로직(signup, signin , login)
 */
@Service
@RequiredArgsConstructor
public class ExternalAuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSaveResponse signup(UserSaveRequest request) { // 회원기입 서비스 로직


        if (userRepository.existsByEmail((request.email()))) { //이메일 중복 검증
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }

        if (userRepository.existsByUserName((request.username()))) { //유저이름 중복 검증
            throw new UserException(UserErrorCode.ALREADY_EXIST_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(request.password()); //비밀번호 암호화
        User user = userMapper.toEntity(request, encodedPassword); // 엔티티 변환
        User saveUser = userRepository.save(user);

        UserSaveResponse userSaveResponse = userMapper.toDto(saveUser); //디티오 변환
        return userSaveResponse;

    }
}
