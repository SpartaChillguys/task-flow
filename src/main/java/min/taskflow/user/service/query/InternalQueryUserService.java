package min.taskflow.user.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.exception.UserErrorCode;
import min.taskflow.user.exception.UserException;
import min.taskflow.user.mapper.UserMapper;
import min.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/*
다른 도메인에서 참조할 메서드들 모음집
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InternalQueryUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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


    public UserSearchAndAssigneeResponse getAssigneeByUserId(Long userId) {

        User byUserId = findByUserId(userId);
        UserSearchAndAssigneeResponse userSearchAndAssigneeResponse = userMapper.toSearchAndAssigneeResponse(byUserId);

        return userSearchAndAssigneeResponse;
    }
    // 팀이 없는 유저 조회

    public List<UserResponse> findByTeamIsNull() {

        List<User> users = userRepository.findByTeamIsNull();
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    // 전체 유저 조회

    public List<UserResponse> findAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    //유저 이름 다 조회

    public List<String> findAllUserNames() {

        return userRepository.findAll()
                .stream()
                .map(User::getName)
                .toList();
    }


    //이름 검색을 했을때 포함된 결과 반환
    public List<UserSearchAndAssigneeResponse> findUsersByName(String query) {

        //검색어 포함결과 반환
        List<User> users = userRepository.findByNameContaining(query);

        List<UserSearchAndAssigneeResponse> responses = new ArrayList<>();

        for (User user : users) {
            responses.add(userMapper.toSearchAndAssigneeResponse(user));
        }

        return responses;

    }
}
