package com.example.youtubeSheet.post;

import com.example.youtubeSheet.PostImageService;
import com.example.youtubeSheet.post.dto.PostImageDto;
import com.example.youtubeSheet.post.entitiy.Post;
import com.example.youtubeSheet.post.entitiy.PostImage;
import com.example.youtubeSheet.post.repository.PostImageRepository;
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
    public void save(List<MultipartFile> multipartFileList, Post post) throws IOException {
        for(MultipartFile file: multipartFileList){
            if(file.getSize()>0){

                if(post.getFileAttached()==0) post.setFileAttached(1);

                String originalFileName=file.getOriginalFilename();
                String storedFileName=System.currentTimeMillis()+"_"+originalFileName;
                String savePath="C:/Users/Hwa/springbootImg/sheets/"+storedFileName;
                file.transferTo(new File(savePath));
                PostImage postImage =new PostImage();
                postImage.setOriginalFileName(originalFileName);
                postImage.setStoredFileName(storedFileName);
                postImage.setPost(post);
                this.postImageRepository.save(postImage);


            }
        }

    }

}
