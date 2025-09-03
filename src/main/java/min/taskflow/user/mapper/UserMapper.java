package min.taskflow.user.mapper;

import lombok.RequiredArgsConstructor;
import min.taskflow.user.dto.UserSaveRequest;
import min.taskflow.user.dto.UserSaveResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    //UserSaveRequest DTO를 User Entity로 변환
    public User toEntity(UserSaveRequest request, String encodedPassword) {
        return User.builder()
                .userName(request.username())
                .password(encodedPassword)
                .email(request.email())
                .name(request.name())
                .role(UserRole.USER)  //기본값 USER
                .team(null) //기본값 NULL
                .build();
    }

    public UserSaveResponse toDto(User user) {
        return UserSaveResponse.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
