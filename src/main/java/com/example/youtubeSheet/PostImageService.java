package com.example.youtubeSheet;

import com.example.youtubeSheet.post.entitiy.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostImageService {

    void save(List<MultipartFile> multipartFileList, Post post) throws IOException;

}
