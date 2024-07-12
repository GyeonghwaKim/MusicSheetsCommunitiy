package com.example.youtubeSheet.comment;


import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.comment.dto.CommentForm;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import com.example.youtubeSheet.post.PostService;
import com.example.youtubeSheet.user.siteuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Slf4j
@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated")
    @PostMapping("/create")
    public String createComment(@Valid @ModelAttribute(name="commentForm") CommentForm commentForm, BindingResult bindingResult
            , Model model, @RequestParam("postId") Long postId, Principal principal){

        SiteUserDto siteUserDto =this.userService.getUser(principal.getName());
        PostDto postDto =this.postService.getPost(postId);

        if(bindingResult.hasErrors()){
            model.addAttribute("post",postDto);
            return "/post/postDetail";
        }

        this.commentService.create(postDto.getId(),commentForm.getContent(),siteUserDto);

        return "redirect:/post/detail/"+postId;

    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id")Long commentId,Principal principal){

        CommentDto commentDto=this.commentService.getComment(commentId);
        Long postId=commentDto.getPostId();

        if (!commentDto.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.commentService.delete(commentDto);

        return "redirect:/post/detail/"+postId;

    }
}
