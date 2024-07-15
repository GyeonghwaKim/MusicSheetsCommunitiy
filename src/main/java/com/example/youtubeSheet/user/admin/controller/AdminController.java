package com.example.youtubeSheet.user.admin.controller;


import com.example.youtubeSheet.profileImage.service.ProfileImageService;
import com.example.youtubeSheet.comment.service.CommentService;
import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.post.service.PostService;
import com.example.youtubeSheet.post.dto.PostDto;;
import com.example.youtubeSheet.user.siteuser.service.UserService;
import com.example.youtubeSheet.profileImage.dto.ProfileImageDto;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ProfileImageService profileImageService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String adminHome(Model model){

        Long userCount=this.userService.getTotalUserCount();
        model.addAttribute("userCount",userCount);

        Long postCount=this.postService.getTotalCountPosts();
        model.addAttribute("postCount",postCount);

        Long commentCount=this.commentService.getTotalCountComments();
        model.addAttribute("commentCount",commentCount);

        return"/admin/dashBoard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/post")
    public String adminPost(Model model,@RequestParam(value="page",defaultValue = "0") int page){
        Page<PostDto> paging=this.postService.getPagedPosts(page);

        paging.forEach(postDto -> postDto.setCommentList(
                this.commentService.getCommentsByPostId(postDto.getId())
        ));

        model.addAttribute("paging",paging);
        return "/admin/post";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/post/search")
    public String searchPost(Model model,@RequestParam(value = "page",defaultValue = "0") int page,
                             @RequestParam("keyword")String keyword){

        Page<PostDto> paging=this.postService.searchPagedPosts(page,keyword);

        model.addAttribute("paging",paging);

        return "/admin/post";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/comment")
    public String adminComment(Model model,@RequestParam(value="page",defaultValue = "0") int page){
        Page<CommentDto> paging=this.commentService.getPagedComments(page);

        paging.forEach(commentDto -> commentDto.setPostTitle(
                        this.postService.getPostTitleById(commentDto.getPostId())));

       model.addAttribute("paging",paging);

        return "/admin/comment";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/comment/search")
    public String searchComment(Model model,@RequestParam(value = "page",defaultValue = "0") int page,
                             @RequestParam("keyword")String keyword){

        Page<CommentDto> paging=this.commentService.searchPagedComments(page,keyword);

        model.addAttribute("paging",paging);
        return "/admin/comment";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/account")
    public String adminAccount(Model model,@RequestParam(value = "page", defaultValue = "0")int page){

        Page<SiteUserDto> paging=this.userService.getPagedUsers(page);

        paging.forEach(siteUserDto -> {
                    siteUserDto.setPostList(this.postService.getPostsByAuthorId(siteUserDto.getId()));
                    siteUserDto.setCommentList(this.commentService.getCommentsByAuthorId(siteUserDto.getId()));
                });

        model.addAttribute("paging",paging);
        model.addAttribute("account",new SiteUserDto());

        return "/admin/account";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/account/search")
    public String searchAccount(Model model,@RequestParam(value = "page",defaultValue = "0") int page,
                                @RequestParam("keyword")String keyword){
        Page<SiteUserDto> paging=this.userService.searchPagedUsers(page,keyword);

        paging.forEach(siteUserDto -> {
            siteUserDto.setPostList(this.postService.getPostsByAuthorId(siteUserDto.getId()));
            siteUserDto.setCommentList(this.commentService.getCommentsByAuthorId(siteUserDto.getId()));
        });

        model.addAttribute("paging",paging);
        return "/admin/account";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<?> userProfile(@PathVariable("userId")Long userId){
        SiteUserDto siteUserDto=this.userService.getUserById(userId);
        if (siteUserDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        ProfileImageDto profileImageDto =this.profileImageService.getProfileImage(siteUserDto.getProfileImgId());
        if (profileImageDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile Img Not Found");
        }

        SiteUserDto accountDto=new SiteUserDto();
        accountDto.setId(userId);
        accountDto.setUsername(siteUserDto.getUsername());
        accountDto.setEmail(siteUserDto.getEmail());
        accountDto.setStoredImgName(profileImageDto.getStoredImgName());

        return ResponseEntity.ok(accountDto);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userPost/{userId}")
    public ResponseEntity<?> userPost(@PathVariable("userId")Long userId,
                               @RequestParam(value = "page",defaultValue = "0") int postPage){

        SiteUserDto siteUserDto = this.userService.getUserById(userId);
        if (siteUserDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        Page<PostDto> pagedPostDtoList=this.postService.getPagedPostsByAuthorId(userId,postPage);

        siteUserDto.setPagedPostList(pagedPostDtoList);

        return ResponseEntity.ok(siteUserDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userComment/{userId}")
    public ResponseEntity<?> userComment(@PathVariable("userId")Long userId, @RequestParam(value = "page",defaultValue = "0") int commentPage){

        SiteUserDto siteUserDto = this.userService.getUserById(userId);
        if (siteUserDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        Page<CommentDto> pagedCommentDtoList=this.commentService.getPagedCommentsByAuthorId(userId,commentPage);
        pagedCommentDtoList.forEach(commentDto ->
                commentDto.setPostTitle(this.postService.getPostTitleById(commentDto.getPostId())));

        siteUserDto.setPagedCommentList(pagedCommentDtoList);

        return ResponseEntity.ok(siteUserDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId")Long postId){
        PostDto postDto=this.postService.getPost(postId);
        try{
            this.postService.deletePost(postDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail To Delete A Post");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<?> deletedComment(@PathVariable("commentId")Long commentId){
        CommentDto commentDto=this.commentService.getComment(commentId);
        try{
            this.commentService.delete(commentDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail To Delete A Comment");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/comments/delete")
    public ResponseEntity<?> checkedCommentsDelete(@RequestBody Map<String, List<String>> payload){

        List<String> checkedComments=payload.get("checkedComments");


        if( checkedComments ==null || checkedComments.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Comments selected");
        }

        this.commentService.deleteCheckedComments(checkedComments);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/posts/delete")
    public ResponseEntity<?> checkedPostsDelete(@RequestBody Map<String, List<String>> payload){

        List<String> checkedPosts=payload.get("checkedPosts");

        if( checkedPosts ==null || checkedPosts.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Posts selected");
        }

        this.postService.deleteCheckedPosts(checkedPosts);

        return ResponseEntity.ok().build();
    }

}
