package com.example.youtubeSheet.postImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.postImage.repository.PostImageRepository;
import com.example.youtubeSheet.postImage.dto.PostImageDto;
import com.example.youtubeSheet.postImage.entity.PostImage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Profile("prod")
@RequiredArgsConstructor
@Service
public class PostImageS3Service implements PostImageService {

    private final AmazonS3 s3Client;

    private final PostImageRepository postImageRepository;

    private final ModelMapper modelMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private PostImageDto of(PostImage postImage){
        return modelMapper.map(postImage, PostImageDto.class);
    }
    private PostImage of(PostImageDto postImageDto){
        return modelMapper.map(postImageDto, PostImage.class);
    }



    @Override
    public List<PostImageDto> save(PostDto postDto, List<MultipartFile> multipartFileList,List<String> deleteFileJsonList) throws IOException {

        List<PostImageDto> savePostImageDtoList=postDto.getPostImageList();

        if(deleteFileJsonList !=null && !deleteFileJsonList.isEmpty()){
            ObjectMapper objectMapper=new ObjectMapper();
            String json=deleteFileJsonList.toString();
            List<PostImageDto> deleteImageList=objectMapper.readValue(json, new TypeReference<List<PostImageDto>>(){});

            if(postDto.getPostImageList().size() == deleteImageList.size()) postDto.setFileAttached(0);

            if(!deleteImageList.isEmpty()) deleteImageList.forEach(postImageDto -> savePostImageDtoList.remove(postImageDto));

        }


        for(MultipartFile image: multipartFileList){

            if(image.getSize()>0){

                if(postDto.getFileAttached()==0) postDto.setFileAttached(1);

                String originalFileName=image.getOriginalFilename();
                String storedImagePath="post/"+this.changeFileName(originalFileName);
                String storedFileName="/s3Post/"+storedImagePath;

                ObjectMetadata metadata=new ObjectMetadata();
                metadata.setContentType(image.getContentType());
                metadata.setContentLength(image.getSize());
                s3Client.putObject(bucket,storedImagePath, image.getInputStream(), metadata);

                PostImageDto postImageDto =new PostImageDto();
                postImageDto.setOriginalFileName(originalFileName);
                postImageDto.setStoredFileName(storedFileName);
                PostImage savePostImage=this.postImageRepository.save(of(postImageDto));

                savePostImageDtoList.add(of(savePostImage));

            }
        }

        return savePostImageDtoList;
    }


    private String changeFileName(String originalImageName){
        return System.currentTimeMillis()+"_"+originalImageName;
    }

}
