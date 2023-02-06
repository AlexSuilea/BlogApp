package com.springboot.blog.service.impl;

import com.springboot.blog.domain.Comment;
import com.springboot.blog.domain.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post post = postService.getPostByIdEntity(postId);

        comment.setPost(post);

        commentRepository.save(comment);

        return mapToDTO(comment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDTO(comment)).toList();
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Comment comment = getComment(postId, commentId);
        setCommentEntity(commentDto, comment);

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = getComment(postId, commentId);
        commentRepository.delete(comment);
    }

    private Comment getComment(Long postId, Long commentId) {
        Post post = postService.getPostByIdEntity(postId);
        Comment comment = getCommentByIdEntity(commentId);
        validateComment(post, comment);
        return comment;
    }

    private static void validateComment(Post post, Comment comment) {
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to the post");
        }
    }

    private Comment getCommentByIdEntity(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

    private CommentDto mapToDTO(Comment comment) {
        return mapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return mapper.map(commentDto, Comment.class);
    }

    private void setCommentEntity(CommentDto commentDto, Comment comment) {
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
    }
}
