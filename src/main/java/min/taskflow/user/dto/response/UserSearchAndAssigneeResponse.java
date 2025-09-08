package min.taskflow.user.dto.response;

import lombok.Builder;

/*
유저 검색 응답과 Assignee 응답이 똑같아서 두 요청에 대한 공통 응답
 */
@Builder
public record UserSearchAndAssigneeResponse(Long id,
                                            String username,
                                            String name,
                                            String email) {
}
