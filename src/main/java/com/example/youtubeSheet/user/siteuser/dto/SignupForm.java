package com.example.youtubeSheet.user.siteuser.dto;


import com.example.youtubeSheet.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {


    @NotBlank(message = "사용자 ID는 필수 항목입니다")
    private String username;


    @Password
    @NotBlank(message = "비밀번호는 필수 항목입니다")
    private String password;

    @NotBlank(message = "비밀번호 확인 필수 항목입니다")
    private String confirmPassword;

    @Email
    @NotBlank(message = "이메일은 필수 항목입니다")
    private String email;


    public SignupForm() {
    }
}
