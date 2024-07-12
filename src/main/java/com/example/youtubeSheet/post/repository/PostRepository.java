package com.example.youtubeSheet.post.repository;

import com.example.youtubeSheet.post.entitiy.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByTitleContainingOrAuthorUsernameContaining(String title, String username, Pageable pageable);

    List<Post> findAllByAuthorId(Long authorId);

    Page<Post> findAllByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT p.title FROM Post p WHERE p.id = :id")
    String getTitleById(@Param("id") Long postId);
}
