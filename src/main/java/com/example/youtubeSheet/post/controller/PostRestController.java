package com.example.youtubeSheet.post.controller;

import com.example.youtubeSheet.comment.CommentService;
import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.post.PostService;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.post.dto.PostFileDto;
import com.example.youtubeSheet.user.siteuser.service.ProfileImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostService postService;


    private final CommentService commentService;




}
