package com.example.youtubeSheet.post;

import com.example.youtubeSheet.PostImageService;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.post.dto.PostImageDto;
import com.example.youtubeSheet.post.entitiy.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Profile("prod")
@RequiredArgsConstructor
@Service
public class PostImageS3Service implements PostImageService {

    @Override
    public List<PostImageDto> save(PostDto postDto, List<MultipartFile> multipartFileList,List<String> deleteFileJsonList) throws IOException {

return null;
    }
}
