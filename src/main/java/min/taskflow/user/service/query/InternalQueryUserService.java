package min.taskflow.user.service.query;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
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

    public User getUserByUserId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return user;
    }

    public String getUserNameByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String userName = user.getName();

        return userName;
    }

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserSearchAndAssigneeResponse getAssigneeByUserId(Long userId) {

        User byUserId = getUserByUserId(userId);
        UserSearchAndAssigneeResponse userSearchAndAssigneeResponse = userMapper.toSearchAndAssigneeResponse(byUserId);

        return userSearchAndAssigneeResponse;
    }

    public AssigneeSummaryResponse getAssigneeSummaryByUserId(Long userId) {

        User byUserId = getUserByUserId(userId);
        AssigneeSummaryResponse assigneeSummaryResponse = userMapper.toAssigneeSummaryResponse(byUserId);

        return assigneeSummaryResponse;
    }

    // 팀이 없는 유저 조회
    public List<UserResponse> findByTeamIsNull() {

        List<User> users = userRepository.findByTeamIsNull();
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    // 전체 유저 조회
    public List<UserResponse> findAllUsersAsResponse() {

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }

    // 전체 유저 조회
    public List<User> findAllUsers() {

        List<User> users = userRepository.findAll();
        return users;
    }

    //유저 이름 다 조회
    public List<String> findAllUserNames() {

        return userRepository.findAll()
                .stream()
                .map(User::getName)
                .toList();
    }

    //이름 검색을 했을때 포함된 결과 반환
    public List<UserSearchAndAssigneeResponse> searchUsersByQuery(String query) {

        //검색어 포함결과 반환
        List<User> users = userRepository.findByNameContaining(query);

        List<UserSearchAndAssigneeResponse> responses = new ArrayList<>();

        for (User user : users) {
            responses.add(userMapper.toSearchAndAssigneeResponse(user));
        }

        return responses;
    }

    public UserProfileResponse findByName(String userName) {
        User user = userRepository.findByName((userName)).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        UserProfileResponse userProfileResponse = userMapper.toProfileResponse(user);

        return userProfileResponse;


    }
}
