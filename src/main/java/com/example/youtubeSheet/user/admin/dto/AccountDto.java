package com.example.youtubeSheet.user.admin.dto;


import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime signupDate;

    private String storedImgName;

    private List<PostDto> postList;
    private Page<PostDto> pagedPostList;

    private List<CommentDto> commentList;
    private Page<CommentDto> pagedCommentList;

}
