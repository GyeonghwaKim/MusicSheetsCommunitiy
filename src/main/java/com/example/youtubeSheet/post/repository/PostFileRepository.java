package com.example.youtubeSheet.post.repository;

import com.example.youtubeSheet.post.entitiy.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile,Long> {
}
