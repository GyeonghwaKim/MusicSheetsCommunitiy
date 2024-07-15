package com.example.youtubeSheet.profileImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.profileImage.dto.ProfileImageDto;
import com.example.youtubeSheet.profileImage.Entity.ProfileImage;
import com.example.youtubeSheet.profileImage.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Profile("prod")
@RequiredArgsConstructor
@Service
public class ProfileImageS3Service implements ProfileImageService {

    private final AmazonS3 s3Client;

    private final ProfileImageRepository profileImageRepository;

    private final ModelMapper modelMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private ProfileImageDto of(ProfileImage profileImage){
        return modelMapper.map(profileImage, ProfileImageDto.class);
    }

    private ProfileImage of(ProfileImageDto profileImageDto){
        return modelMapper.map(profileImageDto, ProfileImage.class);
    }




    @Override
    public ProfileImageDto getProfileImage(Long profileImageId) {
        Optional<ProfileImage> optionalProfileImg =this.profileImageRepository.findById(profileImageId);

        if(optionalProfileImg.isPresent()) return of(optionalProfileImg.get());
        else throw new DataNotFoundException("ProfileImage Not Found");
    }

    @Override
    public String uploadProfileImage(MultipartFile image,Long profileImageId) throws IOException {

        String originalImageName=image.getOriginalFilename();
        String storedImagePath ="profile/"+this.changeFileName(originalImageName);
        String storedImageName ="/s3Profile/"+storedImagePath;

        putObjectToS3(image, storedImagePath);

        ProfileImageDto profileImageDto=this.getProfileImage(profileImageId);
        profileImageDto.setOriginalImgName(originalImageName);
        profileImageDto.setStoredImgName(storedImageName);

        this.profileImageRepository.save(of(profileImageDto));

        return s3Client.getUrl(bucket,storedImagePath).toString();

    }


    @Override
    public ProfileImageDto setDefaultProfileImage(Long profileImageId) {

        ProfileImageDto profileImageDto=this.getProfileImage(profileImageId);
        profileImageDto.setOriginalImgName("default/defaultProfile.png");
        profileImageDto.setStoredImgName("/s3Profile/profile/default/defaultProfile.png");

        this.save(profileImageDto);

        return profileImageDto;
    }

    public ProfileImageDto save(ProfileImageDto profileImageDto){
        ProfileImage profileImage=this.profileImageRepository.save(of(profileImageDto));
        return of(profileImage);

    }

    private String changeFileName(String originalImageName){
        return System.currentTimeMillis()+"_"+originalImageName;
    }

    private void putObjectToS3(MultipartFile image, String storedImagePath) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        s3Client.putObject(bucket, storedImagePath, image.getInputStream(), metadata);
    }



}
