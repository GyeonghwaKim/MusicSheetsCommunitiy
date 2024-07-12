package com.example.youtubeSheet.comment;


import com.example.youtubeSheet.comment.entitiy.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Override
    Page<Comment> findAll(Pageable pageable);

    List<Comment> findAllByAuthorId(Long authorId);

    Page<Comment> findAllByAuthorId(Long authorId, Pageable pageable);

    List<Comment> findAllByPostId(Long postId);


    Page<Comment> findByContentContainingOrAuthorUsernameContaining(String title, String username, Pageable pageable);
}
