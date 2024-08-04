package com.example.youtubeSheet.profileImage.service;

import com.example.youtubeSheet.profileImage.dto.ProfileImageDto;
import org.springframework.web.multipart.MultipartFile;


public interface ProfileImageService {

    ProfileImageDto save(ProfileImageDto profileImageDto);

    ProfileImageDto setDefaultProfileImage(Long profileImageId);

    ProfileImageDto getProfileImage(Long profileImageId);


    String uploadProfileImage(MultipartFile image,Long profileImageId);

}
