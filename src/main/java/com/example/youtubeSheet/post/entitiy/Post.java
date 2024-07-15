package com.example.youtubeSheet.post.entitiy;

import com.example.youtubeSheet.postImage.entity.PostImage;
import com.example.youtubeSheet.user.siteuser.entitiy.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    private SiteUser author;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @ManyToMany
    private Set<SiteUser> voter;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int fileAttached;


    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "post_id") // post_id는 PostImage 테이블에 외래 키로 추가됩니다
    private List<PostImage> postImageList=new ArrayList<>();


    //    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
//    private List<PostImage> postImageList =new ArrayList<>();

}
