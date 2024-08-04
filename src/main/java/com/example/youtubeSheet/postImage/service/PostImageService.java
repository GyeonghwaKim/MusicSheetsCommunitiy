package com.example.youtubeSheet.postImage.service;

import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.postImage.dto.PostImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostImageService {

    List<PostImageDto> save(PostDto postDto, List<MultipartFile> multipartFileList, List<String> deleteFileJsonList);

}
