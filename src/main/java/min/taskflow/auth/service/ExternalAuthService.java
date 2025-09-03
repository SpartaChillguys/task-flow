package min.taskflow.auth.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.auth.config.JwtUtil;
import min.taskflow.auth.dto.request.SignInRequest;
import min.taskflow.auth.dto.request.SignupRequest;
import min.taskflow.auth.dto.response.SignInResponse;
import min.taskflow.auth.dto.response.SignupResponse;
import min.taskflow.user.PasswordEncoder;
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
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest request) { // 회원기입 서비스 로직

        //이메일 중복 검증
        if (userRepository.existsByEmail((request.email()))) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }
        //유저이름 중복 검증
        if (userRepository.existsByUserName((request.username()))) {
            throw new UserException(UserErrorCode.ALREADY_EXIST_USERNAME);
        }
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 엔티티 변환
        User user = userMapper.toEntity(request, encodedPassword);
        User saveUser = userRepository.save(user);

        //디티오 변환
        SignupResponse userSaveResponse = userMapper.toDto(saveUser);
        return userSaveResponse;

    }

    @Transactional
    public SignInResponse signin(@Valid SignInRequest request) {

        //유저네임 존재 검증
        User user = userRepository.findByUserName(request.username()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        //비번 일치 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserException(UserErrorCode.WRONG_PASSWORD);
        }
        String token = jwtUtil.createToken(user.getUserId(), user.getRole());

        return new SignInResponse(token);
    }
}
