package com.example.youtubeSheet.post.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostFileDto {
    private Long id;
    private String originalFileName;
    private String storedFileName;

    private Long postId;


}
