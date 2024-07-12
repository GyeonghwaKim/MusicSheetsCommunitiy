package com.example.youtubeSheet.comment.entitiy;


import com.example.youtubeSheet.user.siteuser.entitiy.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    private SiteUser author;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createAt;

    private Long postId;


}
