package com.example.youtubeSheet.comment.service;


import com.example.youtubeSheet.comment.entitiy.Comment;
import com.example.youtubeSheet.comment.repository.CommentRepository;
import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.comment.dto.CommentDto;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private CommentDto of(Comment comment){
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment of(CommentDto commentDto){
        return modelMapper.map(commentDto, Comment.class);
    }

    private Page<CommentDto> convertToCommentDtoPage(Page<Comment> commentPage){
        return commentPage.map(this::of);
    }

    public void create(Long postId, String content, SiteUserDto siteUserDto) {

        CommentDto commentDto=new CommentDto();
        commentDto.setPostId(postId);
        commentDto.setAuthor(siteUserDto);
        commentDto.setContent(content);
        commentDto.setCreateAt(LocalDateTime.now());

        Comment comment=of(commentDto);

        this.commentRepository.save(comment);
    }


    public CommentDto getComment(Long id) {

        Optional<Comment> _comment=this.commentRepository.findById(id);

        if(_comment.isPresent()) return of(_comment.get());
        else throw new DataNotFoundException("Comment Not Found");
    }

    public List<CommentDto> getCommentsByAuthorId(Long authorId) {
        List<Comment> commentList=this.commentRepository.findAllByAuthorId(authorId);
        return commentList.stream().map(this::of).toList();
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        //없을때 예외 던저라
        List<Comment> commentList=this.commentRepository.findAllByPostId(postId);
        return commentList.stream().map(this::of).toList();
    }

    public Page<CommentDto> getPagedCommentsByAuthorId(Long authorId, int page) {
        Pageable pageable = getPageable(page, 5);

        return convertToCommentDtoPage(this.commentRepository.findAllByAuthorId(authorId,pageable));
    }

    public Page<CommentDto> getPagedComments(int page) {

        Pageable pageable = getPageable(page, 10);

        return convertToCommentDtoPage(this.commentRepository.findAll(pageable));

    }

    public Page<CommentDto> searchPagedComments(int page, String keyword) {
        Pageable pageable = getPageable(page, 10);
        return convertToCommentDtoPage(this.commentRepository.findByContentContainingOrAuthorUsernameContaining(keyword,keyword,pageable));

    }


    public void delete(CommentDto commentDto) {
        this.commentRepository.delete(of(commentDto));
    }

    public void deleteCheckedComments(List<String> checkedComments) {

        List<CommentDto> commentList =checkedComments
                .stream()
                .map(s -> getComment(Long.parseLong(s))).toList();

        commentList.forEach(this::delete);
    }


    public Long getTotalCountComments() {
        return this.commentRepository.count();
    }

    private static Pageable getPageable(int page, int pageSize) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createAt"));
        Pageable pageable= PageRequest.of(page, pageSize,Sort.by(sorts));
        return pageable;
    }

}
