package com.example.youtubeSheet.profileImage.service;

import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.profileImage.Entity.ProfileImage;
import com.example.youtubeSheet.profileImage.repository.ProfileImageRepository;
import com.example.youtubeSheet.profileImage.dto.ProfileImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Profile("local")
@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImageLocalService implements ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ModelMapper modelMapper;

    private ProfileImageDto of(ProfileImage profileImage){
        return modelMapper.map(profileImage, ProfileImageDto.class);
    }

    private ProfileImage of(ProfileImageDto profileImageDto){
        return modelMapper.map(profileImageDto, ProfileImage.class);
    }


    @Override
    public ProfileImageDto getProfileImage(Long profileImageId) {
        Optional<ProfileImage> optionalProfileImg =this.profileImageRepository.findById(profileImageId);
        if(optionalProfileImg.isPresent()){
            return of(optionalProfileImg.get());
        }else throw new DataNotFoundException("ProfileImg Not Found");
    }

    @Override
    public String uploadProfileImage(MultipartFile image, Long profileImageId) throws IOException {
        String originalFileName=image.getOriginalFilename();
        String storedFileName="/localProfile/"+System.currentTimeMillis()+"_"+originalFileName;
        String savePath="C:/Users/Hwa/springbootImg/sheets/localProfile/"+storedFileName;
        image.transferTo(new File(savePath));

        ProfileImageDto profileImageDto =this.getProfileImage(profileImageId);
        profileImageDto.setOriginalImgName(originalFileName);
        profileImageDto.setStoredImgName(storedFileName);


        this.profileImageRepository.save(of(profileImageDto));

        return savePath;
    }


    @Override
    public ProfileImageDto setDefaultProfileImage(Long profileImageId) {
        ProfileImageDto profileImageDto =this.getProfileImage(profileImageId);
        profileImageDto.setOriginalImgName("default/defaultProfile.png");
        profileImageDto.setStoredImgName("/localProfile/default/defaultProfile.png");

        this.save(profileImageDto);

        return profileImageDto;
    }

    @Override
    public ProfileImageDto save(ProfileImageDto profileImageDto) {
        ProfileImage profileImage=this.profileImageRepository.save(of(profileImageDto));
        return of(profileImage);

    }
}
