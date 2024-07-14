package com.example.youtubeSheet.post;

import com.example.youtubeSheet.PostImageService;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.post.dto.PostImageDto;
import com.example.youtubeSheet.post.entitiy.PostImage;
import com.example.youtubeSheet.post.repository.PostImageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public List<PostImageDto> save(PostDto postDto, List<MultipartFile> multipartFileList,List<String> deleteFileJsonList) throws IOException{


        if(deleteFileJsonList !=null && !deleteFileJsonList.isEmpty()){
            ObjectMapper objectMapper=new ObjectMapper();
            String json=deleteFileJsonList.toString();
            List<PostImageDto> deleteImageList=objectMapper.readValue(json, new TypeReference<List<PostImageDto>>(){});

            if(postDto.getPostImageList().size() == deleteImageList.size()) postDto.setFileAttached(0);

            if(!deleteImageList.isEmpty()) deleteImageList.forEach(postImageDto -> postDto.getPostImageList().remove(postImageDto));

        }
        List<PostImageDto> savePostImageDtoList=postDto.getPostImageList();

        for(MultipartFile file: multipartFileList){
            if(file.getSize()>0){

                if(postDto.getFileAttached()==0) postDto.setFileAttached(1);

                String originalFileName=file.getOriginalFilename();
                String storedFileName=System.currentTimeMillis()+"_"+originalFileName;
                String savePath="C:/Users/Hwa/springbootImg/sheets/"+storedFileName;
                file.transferTo(new File(savePath));

                PostImageDto postImageDto =new PostImageDto();
                postImageDto.setOriginalFileName(originalFileName);
                postImageDto.setStoredFileName(storedFileName);
                PostImage savePostImage=this.postImageRepository.save(of(postImageDto));
                savePostImageDtoList.add(of(savePostImage));

            }
        }

        return savePostImageDtoList;

    }

}
