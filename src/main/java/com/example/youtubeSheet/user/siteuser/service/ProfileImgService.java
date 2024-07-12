package com.example.youtubeSheet.user.siteuser.service;

import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.user.siteuser.repository.ProfileImgRepository;
import com.example.youtubeSheet.user.siteuser.repository.UserRepository;
import com.example.youtubeSheet.user.siteuser.dto.ProfileImgDto;
import com.example.youtubeSheet.user.siteuser.entitiy.ProfileImg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImgService {

    private final ProfileImgRepository profileImgRepository;
    private final ModelMapper modelMapper;

    private ProfileImgDto of(ProfileImg profileImg){
        return modelMapper.map(profileImg, ProfileImgDto.class);
    }

    private ProfileImg of(ProfileImgDto profileImgDto){
        return modelMapper.map(profileImgDto, ProfileImg.class);
    }

    public ProfileImgDto getProfileImg(Long profileImgId) {
        Optional<ProfileImg> optionalProfileImg =this.profileImgRepository.findById(profileImgId);
        if(optionalProfileImg.isPresent()){
            return of(optionalProfileImg.get());
        }else throw new DataNotFoundException("ProfileImg Not Found");

    }

    public void changeProfileImg(MultipartFile multipartFile, Long profileImgId) throws IOException {

        String originalFileName=multipartFile.getOriginalFilename();
        String storedFileName=System.currentTimeMillis()+"_"+originalFileName;
        String savePath="C:/Users/Hwa/springbootImg/sheets/profiles/"+storedFileName;
        multipartFile.transferTo(new File(savePath));

        ProfileImgDto profileImgDto=getProfileImg(profileImgId);
        profileImgDto.setOriginalImgName(originalFileName);
        profileImgDto.setStoredImgName(storedFileName);

        this.profileImgRepository.save(of(profileImgDto));

    }


    public void changeDefaultProfileImg(Long profileImgId) {

        ProfileImgDto profileImgDto=getProfileImg(profileImgId);
        profileImgDto.setOriginalImgName("default/defaultProfile.png");
        profileImgDto.setStoredImgName("default/defaultProfile.png");

        this.profileImgRepository.save(of(profileImgDto));
    }
}
