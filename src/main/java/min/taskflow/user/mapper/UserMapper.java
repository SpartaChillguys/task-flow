package min.taskflow.user.mapper;

import lombok.RequiredArgsConstructor;
import min.taskflow.auth.dto.request.RegisterRequest;
import min.taskflow.auth.dto.response.RegisterResponse;
import min.taskflow.user.dto.response.AssigneeSummaryResponse;
import min.taskflow.user.dto.response.UserProfileResponse;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.dto.response.UserSearchAndAssigneeResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserResponse userResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }


    //UserSaveRequest DTO를 User Entity로 변환
    public User toEntity(RegisterRequest request, String encodedPassword) {

        return User.builder()
                .userName(request.username())
                .password(encodedPassword)
                .email(request.email())
                .name(request.name())
                .role(UserRole.USER)  //기본값 USER
                .team(null) //기본값 NULL
                .build();
    }

    public RegisterResponse toRegistResponse(User user) {

        return RegisterResponse.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserProfileResponse toProfileResponse(User user) {

        return UserProfileResponse.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserSearchAndAssigneeResponse toSearchAndAssigneeResponse(User user) {

        return UserSearchAndAssigneeResponse.builder()
                .userid(user.getUserId())
                .userName(user.getUserName())
                .name(user.getName())
                .email((user.getEmail()))
                .build();
    }

    public AssigneeSummaryResponse toAssigneeSummaryResponse(User user) {

        return AssigneeSummaryResponse.builder()
                .id(user.getUserId())
                .name(user.getName())
                .build();
    }
}
