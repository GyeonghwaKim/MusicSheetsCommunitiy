package com.example.youtubeSheet.post.repository;

import com.example.youtubeSheet.post.entitiy.Post;
import com.example.youtubeSheet.post.entitiy.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {

}
