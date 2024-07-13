package com.example.youtubeSheet.post.controller;

import com.example.youtubeSheet.comment.CommentService;
import com.example.youtubeSheet.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
@RestController
public class PostRestController {

    private final PostService postService;


    private final CommentService commentService;




}
