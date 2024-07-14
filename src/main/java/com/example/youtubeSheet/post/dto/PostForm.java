package com.example.youtubeSheet.post.dto;

import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostForm {

    private Long id;

    @NotBlank(message = "제목은 필수 항목입니다")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다")
    private String content;

    private SiteUserDto author;

    private List<MultipartFile> multipartFileList;
    private List<String> originalNameList;
    private List<String> stroedNameList;

    private List<PostImageDto> postImageList =new ArrayList<>();
    private List<String> postImageJsonList=new ArrayList<>();
    
}
