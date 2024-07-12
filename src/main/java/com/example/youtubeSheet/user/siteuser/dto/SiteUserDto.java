package com.example.youtubeSheet.user.siteuser.dto;

import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserDto {
    private Long id;
    private String username;
    //dto의 패스워드를 쓸 일이 있는지 생각 해보기
    private String password;
    private String email;

    private Long profileImgId;

    private String storedImgName;

    private List<PostDto> postList;
    private Page<PostDto> pagedPostList;

    private List<CommentDto> commentList;
    private Page<CommentDto> pagedCommentList;
}
