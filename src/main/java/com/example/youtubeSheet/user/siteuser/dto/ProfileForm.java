package com.example.youtubeSheet.user.siteuser.dto;

import com.example.youtubeSheet.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileForm {
    private String username;

    @NotBlank(message = "이메일이 공백일 수 없습니다")
    @Email(message = "이메일 형식과 맞지 않습니다")
    private String email;

    @Password
    @NotBlank(message = "비밀번호가 공백일 수 없습니다")
    private String password;

    @NotBlank(message = "확인 비밀번호가 공백일 수 없습니다")
    private String confirmPassword;

    private String storedFileName;

}
