package min.taskflow.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//회원가입시 request DTO
public record UserSaveRequest(
        @NotBlank
        @Size(min = 6, max = 10)
        String username,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$",
                message = "비밀번호는 8~20자이며, 소문자/숫자/특수기호를 모두 포함해야 합니다.")
        String password,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String name) {

}
