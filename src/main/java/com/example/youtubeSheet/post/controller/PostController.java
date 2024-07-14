package com.example.youtubeSheet.post.controller;


import com.example.youtubeSheet.profileImage.service.ProfileImageService;
import com.example.youtubeSheet.comment.CommentService;
import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.comment.dto.CommentForm;
import com.example.youtubeSheet.post.PostService;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.post.dto.PostForm;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import com.example.youtubeSheet.user.siteuser.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ProfileImageService profileImageService;


    @GetMapping("/list")
    public String postList(Model model,@RequestParam(value = "page",defaultValue = "0") int page){
        Page<PostDto> paging=this.postService.getPagedPosts(page);

        setCommentList(paging);

        model.addAttribute("paging",paging);
        return "/post/postList";
    }

    @GetMapping("/search")
    public String searchPost(Model model,@RequestParam(value = "page",defaultValue = "0") int page,
                           @RequestParam("keyword")String keyword){

        Page<PostDto> paging=this.postService.searchPagedPosts(page,keyword);
        setCommentList(paging);

        model.addAttribute("paging",paging);
        return "/post/postList";
    }

    @GetMapping("/detail/{postId}")
    public String postDetail(Model model,@PathVariable(name = "postId")Long postId){

        PostDto postDto=this.postService.getPost(postId);
        List<CommentDto> commentDtoList=this.commentService.getCommentsByPostId(postId);

        model.addAttribute("post",postDto);
        model.addAttribute("commentList",commentDtoList);
        model.addAttribute("commentForm",new CommentForm());
        return "/post/postDetail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createPost(Model model)
    {
        model.addAttribute("postForm",new PostForm());
        return "/post/postForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("postForm") PostForm postForm, BindingResult bindingResult,
                             Principal principal) throws IOException {

        if(bindingResult.hasErrors()) return "/post/postForm";

        SiteUserDto siteUserDto=this.userService.getUser(principal.getName());
        PostDto postDto=this.postService.create(postForm.getTitle(), postForm.getContent(),postForm.getMultipartFileList(),siteUserDto);

        return "redirect:/post/detail/" + postDto.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyPost(Model model,@PathVariable(name = "id") Long id,Principal principal){

        PostDto postDto=this.postService.getPost(id);

        deleteValidation(principal, postDto);

        PostForm postForm=new PostForm();
        postForm.setId(postDto.getId());
        postForm.setTitle(postDto.getTitle());
        postForm.setContent(postDto.getContent());
        model.addAttribute("postForm",postForm);

        return "/post/postForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{postId}")
    public String modifyPost(@Valid @ModelAttribute("postForm")PostForm postForm,BindingResult bindingResult, @PathVariable(name = "postId") Long postId, Principal principal) throws IOException {

        if(bindingResult.hasErrors()) return "/post/postForm";

        PostDto postDto=this.postService.getPost(postId);

        deleteValidation(principal, postDto);

        this.postService.modify(postDto,postForm,postForm.getMultipartFileList(),postForm.getPostImageJsonList());

        return "redirect:/post/detail/" + postId;
    }




    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable(name = "postId") Long id,Principal principal){

        PostDto postDto=this.postService.getPost(id);

        deleteValidation(principal, postDto);

        this.postService.deletePost(postDto);

        return "redirect:/post/list?page=0";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String postVote(Principal principal, @PathVariable("id") Long id) {
        PostDto postDto = this.postService.getPost(id);
        SiteUserDto siteUserDto = this.userService.getUser(principal.getName());

        this.postService.vote(postDto, siteUserDto);
        return "redirect:/post/detail/"+id;
    }



    @GetMapping("/files/{postId}")
    public ResponseEntity<?> findPostFiles(@PathVariable("postId")Long id){

        PostDto postDto=this.postService.getPost(id);
        if (postDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Post");
        }
        return ResponseEntity.ok(postDto.getPostImageList());
    }

    @GetMapping("/comment/profileImage/{postId}")
    public ResponseEntity<?> findCommentProfileImgStoredNameList(@PathVariable("postId")Long id){
        PostDto postDto=this.postService.getPost(id);
        if (postDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Post");
        }

        List<CommentDto> commentList=this.commentService.getCommentsByPostId(postDto.getId());


        List<String> commentProfileImgStoredNameList = commentList.stream()
                .map(commentDto -> commentDto.getAuthor().getProfileImgId())
                .map(profileImgId -> this.profileImageService.getProfileImage(profileImgId).getStoredImgName())
                .toList();


        commentProfileImgStoredNameList.forEach(System.out::println);

        return ResponseEntity.ok(commentProfileImgStoredNameList);
    }

    private static void deleteValidation(Principal principal, PostDto postDto) {
        if(!postDto.getAuthor().getUsername().equals(principal.getName())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }

    private void setCommentList(Page<PostDto> paging) {
        paging.forEach(postDto ->
                postDto.setCommentList(this.commentService.getCommentsByPostId(postDto.getId())));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        String json = "{\"id\":32, \"originalFileName\":\"1.png\", \"storedFileName\":\"1720950025164_1.png\"}";

        List<String> jsonList=new ArrayList<>(List.of(json));

        log.info("list ={}",jsonList);

        ObjectMapper objectMapper=new ObjectMapper();

        List<Test> testList=
        jsonList.stream().map(s -> {
            try {
                log.info("s ={}",s);
                return objectMapper.readValue(s,Test.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return ResponseEntity.ok(testList);

    }
    class Test{
        private Long id;
        private String originalFileName;
        private String storedFileName;

        public Test() {
        }

        public Test(Long id, String originalFileName, String storedFileName) {
            this.id = id;
            this.originalFileName = originalFileName;
            this.storedFileName = storedFileName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOriginalFileName() {
            return originalFileName;
        }

        public void setOriginalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
        }

        public String getStoredFileName() {
            return storedFileName;
        }

        public void setStoredFileName(String storedFileName) {
            this.storedFileName = storedFileName;
        }
    }

}
