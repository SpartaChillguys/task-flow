package min.taskflow.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/*
계정 삭제를 위한 패스워드 요청
 */
public record DeleteRequest(@NotBlank(message = "비밀번호는 공백일 수 없습니다.") String password) {
}
