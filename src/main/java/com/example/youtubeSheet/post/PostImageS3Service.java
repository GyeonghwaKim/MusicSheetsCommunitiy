package com.example.youtubeSheet.post;

import com.example.youtubeSheet.PostImageService;
import com.example.youtubeSheet.post.entitiy.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class PostImageS3Service implements PostImageService {

    @Override
    public void save(List<MultipartFile> multipartFileList, Post post) throws IOException {

    }
}
