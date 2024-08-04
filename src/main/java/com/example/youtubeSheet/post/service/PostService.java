package com.example.youtubeSheet.post.service;


import com.example.youtubeSheet.postImage.service.PostImageService;
import com.example.youtubeSheet.comment.service.CommentService;
import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.post.dto.PostDto;
import com.example.youtubeSheet.post.dto.PostForm;
import com.example.youtubeSheet.post.entitiy.Post;
import com.example.youtubeSheet.post.repository.PostRepository;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final CommentService commentService;

    private final PostRepository postRepository;

    private final PostImageService postImageService;

    private final ModelMapper modelMapper;


    private PostDto of(Post post){
        return modelMapper.map(post,PostDto.class);
    }

    private Post of(PostDto postDto){
        return modelMapper.map(postDto,Post.class);
    }

    private Page<PostDto> convertToPostDtoPage(Page<Post> postPage){
        return postPage.map(this::of);
    }

    public PostDto getPost(Long postId) {

        Optional<Post> _post=this.postRepository.findById(postId);

        if(_post.isPresent()) return of(_post.get());
        else throw new DataNotFoundException("Post Not Found");

    }

    public List<PostDto> getPostsByAuthorId(Long authorId) {
        List<Post> postList=this.postRepository.findAllByAuthorId(authorId);

        return postList.stream().map(this::of).toList();
    }

    public Page<PostDto> getPagedPosts(int page){
        Pageable pageable = getPageable(page, 10);
        return convertToPostDtoPage(this.postRepository.findAll(pageable));
    }

    public Page<PostDto> getPagedPostsByAuthorId(Long authorId, int page) {
        Pageable pageable = getPageable(page, 5);

        return convertToPostDtoPage(this.postRepository.findAllByAuthorId(authorId,pageable));
    }


    public Page<PostDto> searchPagedPosts(int page, String keyword){
        Pageable pageable = getPageable(page, 10);
        return convertToPostDtoPage(this.postRepository.findByTitleContainingOrAuthorUsernameContaining(keyword,keyword,pageable));

    }


    public PostDto create(String title,String content,List<MultipartFile> multipartFileList, SiteUserDto siteUserDto){

        PostDto postDto=new PostDto();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setAuthor(siteUserDto);
        postDto.setCreateAt(LocalDateTime.now());
        postDto.setPostImageList(this.postImageService.save(postDto,multipartFileList,null));

        Post savePost=this.postRepository.save(of(postDto));

        return of(savePost);

    }



    public PostDto modify(PostDto postDto, PostForm postForm, List<MultipartFile> multipartFileList, List<String> deleteImageJsonList){

        postDto.setTitle(postForm.getTitle());
        postDto.setContent(postForm.getContent());
        postDto.setUpdateAt(LocalDateTime.now());

        postDto.setPostImageList(this.postImageService.save(postDto,multipartFileList,deleteImageJsonList));

        Post modifyPost=this.postRepository.save(of(postDto));

        return of(modifyPost);

    }


    public void deletePost(PostDto postDto) {

        List<CommentDto> commentList=this.commentService.getCommentsByPostId(postDto.getId());
        if(!commentList.isEmpty()) commentList.forEach(this.commentService::delete);

        this.postRepository.delete(of(postDto));
    }

    public void vote(PostDto postDto,SiteUserDto siteUserDto){
        postDto.getVoter().add(siteUserDto);
        this.postRepository.save(of(postDto));
    }


    public Long getTotalCountPosts() {
        return this.postRepository.count();
    }

    public String getPostTitleById(Long postId) {
        return this.postRepository.getTitleById(postId);

    }
    public void deleteCheckedPosts(List<String> checkedPosts) {

        List<PostDto> postList =checkedPosts
                .stream()
                .map(s -> getPost(Long.parseLong(s))).toList();

        postList.forEach(this::deletePost);


    }

    private static Pageable getPageable(int page, int pageSize) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createAt"));
        Pageable pageable= PageRequest.of(page, pageSize,Sort.by(sorts));
        return pageable;
    }

}
