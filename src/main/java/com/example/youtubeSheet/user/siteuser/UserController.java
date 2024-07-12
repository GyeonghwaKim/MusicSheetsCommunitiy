package com.example.youtubeSheet.user.siteuser;


import com.example.youtubeSheet.user.siteuser.dto.ProfileForm;
import com.example.youtubeSheet.user.siteuser.dto.ProfileImgDto;
import com.example.youtubeSheet.user.siteuser.dto.SignupForm;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import com.example.youtubeSheet.user.siteuser.service.ProfileImgService;
import com.example.youtubeSheet.user.siteuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {


    private final UserService userService;
    private final ProfileImgService profileImgService;



    @GetMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public String signupUser(SignupForm signupForm){
        return "/siteuser/signupForm";
    }

    @PostMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public String signupUser(@Valid @ModelAttribute("signupForm") SignupForm signupForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()) return "/siteuser/signupForm";

        if(!signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword","passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "/siteuser/signupForm";
        }

        try{
            this.userService.signup(signupForm.getUsername(),signupForm.getEmail(),signupForm.getPassword());

        }catch (DataIntegrityViolationException e){

            e.printStackTrace();
            bindingResult.reject("signupFailed","이미 등록된 사용자입니다");
            return "/siteuser/signupForm";
        }

        catch (Exception e){
            e.printStackTrace();
            bindingResult.rejectValue("signupFailed",e.getMessage());
            return "/siteuser/signupForm";
        }


        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "/siteuser/loginForm";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal){
        SiteUserDto siteUserDto=this.userService.getUser(principal.getName());
        ProfileImgDto profileImgDto=this.profileImgService.getProfileImg(siteUserDto.getProfileImgId());

        ProfileForm profileForm=new ProfileForm();
        profileForm.setUsername(siteUserDto.getUsername());
        profileForm.setEmail(siteUserDto.getEmail());
        profileForm.setStoredFileName(profileImgDto.getStoredImgName());

        model.addAttribute("profileForm",profileForm);

        return "/siteuser/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String profile(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            return "/siteuser/profile";
        }

        if(!profileForm.getConfirmPassword().equals(profileForm.getPassword())){
            bindingResult.rejectValue("confirmPassword","passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "/siteuser/profile";
        }

        try{

            this.userService.changeEmail(profileForm.getUsername(),profileForm.getEmail());
            this.userService.changePassword(profileForm.getUsername(),profileForm.getPassword());

        }catch (DataIntegrityViolationException e){

            e.printStackTrace();
            bindingResult.reject("changeEmail","이미 등록된 이용자입니다");
            return "/siteuser/profile";
        }

        return "redirect:/";

    }



    @PreAuthorize("isAuthenticated")
    @PostMapping("/profileImg")
    public String updateProfileImg(@RequestParam("multipartFile") MultipartFile multipartFile,Principal principal) throws IOException {

        SiteUserDto siteUserDto=this.userService.getUser(principal.getName());

        if(!multipartFile.isEmpty()){
            this.profileImgService.changeProfileImg(multipartFile,siteUserDto.getProfileImgId());
        }else{
            this.profileImgService.changeDefaultProfileImg(siteUserDto.getProfileImgId());
        }

        return "redirect:/profile";
    }




}
