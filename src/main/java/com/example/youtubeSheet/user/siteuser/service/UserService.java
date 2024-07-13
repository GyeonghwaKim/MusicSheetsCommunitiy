package com.example.youtubeSheet.user.siteuser.service;

import com.example.youtubeSheet.profileImage.service.ProfileImageService;
import com.example.youtubeSheet.exception.DataNotFoundException;

import com.example.youtubeSheet.profileImage.dto.ProfileImageDto;
import com.example.youtubeSheet.user.siteuser.repository.UserRepository;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import com.example.youtubeSheet.user.siteuser.entitiy.SiteUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;


    private final ProfileImageService profileImageService;
    private SiteUserDto of(SiteUser siteUser){
        return modelMapper.map(siteUser,SiteUserDto.class);
    }
    private SiteUser of(SiteUserDto siteUser){
        return modelMapper.map(siteUser,SiteUser.class);
    }

    private Page<SiteUserDto> convertToStieUserDtoPage(Page<SiteUser> siteUserPage){
        return siteUserPage.map(this::of);
    }


    @Transactional
    public void signup(String username,String email,String password) {

        SiteUser siteUser =new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);

        String encodePassword=passwordEncoder.encode(password);
        siteUser.setPassword(encodePassword);

        ProfileImageDto profileImage=new ProfileImageDto();
        ProfileImageDto saveProfileImage=this.profileImageService.save(profileImage);
        this.profileImageService.setDefaultProfileImage(saveProfileImage.getId());

        siteUser.setProfileImgId(saveProfileImage.getId());
        siteUser.setSignupDate(LocalDateTime.now());

        this.userRepository.save(siteUser);

    }

    public SiteUserDto getUser(String name) {
        Optional<SiteUser> siteUser=this.userRepository.findByUsername(name);

        if(siteUser.isPresent()){
            return of(siteUser.get());
        }else{
            throw new DataNotFoundException("User Not Found");
        }
    }

    public SiteUserDto getUserById(Long userId) {
        Optional<SiteUser> _siteUser=this.userRepository.findById(userId);

        if(_siteUser.isPresent()) return of(_siteUser.get());
        else throw new DataNotFoundException("User Not Found");
    }

    public Page<SiteUserDto> getPagedUsers(int page) {
        Pageable pageable = getPageable(page, 10);
        return convertToStieUserDtoPage(this.userRepository.findAll(pageable));
    }


    public Page<SiteUserDto> searchPagedUsers(int page, String keyword) {
        Pageable pageable = getPageable(page,10);
        return convertToStieUserDtoPage(
                this.userRepository.findByUsernameContaining(keyword,pageable));
    }

    public void changePassword(String username,String password) {

        SiteUserDto siteUserDto= getUser(username);

        String encodePassword=passwordEncoder.encode(password);
        siteUserDto.setPassword(encodePassword);

        this.userRepository.save(of(siteUserDto));

    }

    public void changeEmail(String username,String email) {

        SiteUserDto siteUserDto= getUser(username);
        siteUserDto.setEmail(email);

        this.userRepository.save(of(siteUserDto));

    }

    public long getTotalUserCount() {
        return userRepository.count();
    }



    private static Pageable getPageable(int page,int pageSize) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("signupDate"));
        Pageable pageable= PageRequest.of(page,pageSize,Sort.by(sorts));
        return pageable;
    }
}