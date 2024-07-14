package com.example.youtubeSheet.post.repository;

import com.example.youtubeSheet.post.entitiy.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostImage,Long> {
}
