package com.example.youtubeSheet.post.dto;


import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostDto {


    private Long id;
    private String title;
    private String content;
    private SiteUserDto author;
    private List<CommentDto> commentList;
    private int commentCnt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Set<SiteUserDto> voter;
    private int fileAttached;
    private List<PostImageDto> postFileList=new ArrayList<>();
}
