package com.example.youtubeSheet.postImage.service;

import com.example.youtubeSheet.exception.DeserializationException;
import com.example.youtubeSheet.exception.MultipartFileException;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.postImage.repository.PostImageRepository;
import com.example.youtubeSheet.postImage.dto.PostImageDto;
import com.example.youtubeSheet.postImage.entity.PostImage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Profile("local")
@RequiredArgsConstructor
@Service
public class PostImageLocalService implements PostImageService {

    private final PostImageRepository postImageRepository;

    private final ModelMapper modelMapper;

    private PostImageDto of(PostImage postImage){
        return modelMapper.map(postImage, PostImageDto.class);
    }
    private PostImage of(PostImageDto postImageDto){
        return modelMapper.map(postImageDto, PostImage.class);
    }


    @Override
    public List<PostImageDto> save(PostDto postDto, List<MultipartFile> multipartFileList,List<String> deleteImageJsonList){

        List<PostImageDto> savePostImageDtoList=postDto.getPostImageList();

        checkDeleteImage(postDto, deleteImageJsonList, savePostImageDtoList);

        for(MultipartFile image: multipartFileList){
            if(image.getSize()>0){

                if(postDto.getFileAttached()==0) postDto.setFileAttached(1);

                String originalFileName=image.getOriginalFilename();
                String storedFileName="/localPost/"+System.currentTimeMillis()+"_"+originalFileName;
                String savePath="C:/Users/Hwa/springbootImg/sheets"+storedFileName;
                try {
                    image.transferTo(new File(savePath));
                } catch (IOException e) {
                    throw new MultipartFileException( "Failed to process multipart file",e);
                }

                PostImageDto postImageDto =new PostImageDto();
                postImageDto.setOriginalFileName(originalFileName);
                postImageDto.setStoredFileName(storedFileName);

                PostImage savePostImage=this.postImageRepository.save(of(postImageDto));
                savePostImageDtoList.add(of(savePostImage));

            }
        }

        return savePostImageDtoList;

    }

    private static void checkDeleteImage(PostDto postDto, List<String> deleteFileJsonList, List<PostImageDto> savePostImageDtoList){
        if(deleteFileJsonList !=null && !deleteFileJsonList.isEmpty()){
            ObjectMapper objectMapper=new ObjectMapper();
            String json= deleteFileJsonList.toString();
            List<PostImageDto> deleteImageList= null;
            try {
                deleteImageList = objectMapper.readValue(json, new TypeReference<List<PostImageDto>>(){});
            } catch (IOException e) {
                throw new DeserializationException("Failed to deserialization",e);
            }

            if(postDto.getPostImageList().size() == deleteImageList.size()) postDto.setFileAttached(0);

            if(!deleteImageList.isEmpty()) deleteImageList.forEach(savePostImageDtoList::remove);

        }
    }

}
