package com.springboot.blog.service.impl;

import com.springboot.blog.domain.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);
        return mapToDto(newPost);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> postList = posts.getContent();

        List<PostDto> content = postList.stream().map(post -> mapToDto(post)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNumber(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = getPostByIdEntity(id);
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post postToUpdate = getPostByIdEntity(id);
        setPostEntity(postDto, postToUpdate);
        postToUpdate = postRepository.save(postToUpdate);
        return mapToDto(postToUpdate);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = getPostByIdEntity(id);
        postRepository.delete(post);
    }

    public Post getPostByIdEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    private PostDto mapToDto(Post post) {
        return mapper.map(post, PostDto.class);
    }

    private Post mapToEntity(PostDto postDto) {
        return mapper.map(postDto, Post.class);
    }

    private void setPostEntity(PostDto postDto, Post post) {
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
    }
}
