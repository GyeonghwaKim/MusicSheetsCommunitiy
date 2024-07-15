package com.example.youtubeSheet.postImage.repository;

import com.example.youtubeSheet.postImage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {

}
