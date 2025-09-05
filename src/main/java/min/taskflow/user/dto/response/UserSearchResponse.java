package min.taskflow.user.dto.response;

import lombok.Builder;

@Builder
public record UserSearchResponse(Long userid,
                                 String userName,
                                 String name,
                                 String email) {
}
