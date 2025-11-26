package com.vank.careerConnectPlatform.postsService.service;

import com.vank.careerConnectPlatform.postsService.auth.AuthContextHolder;
import com.vank.careerConnectPlatform.postsService.client.ConnectionsServiceClient;
import com.vank.careerConnectPlatform.postsService.dto.PersonDto;
import com.vank.careerConnectPlatform.postsService.dto.PostCreateRequestDto;
import com.vank.careerConnectPlatform.postsService.dto.PostDto;
import com.vank.careerConnectPlatform.postsService.entity.Post;
import com.vank.careerConnectPlatform.postsService.exception.ResourceNotFoundException;
import com.vank.careerConnectPlatform.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsServiceClient connectionsServiceClient;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with id: {}", userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);

        Long userId = AuthContextHolder.getCurrentUserId();

//        TODO: Remove in future
//        Call the Connections service fro the Posts service and pass the userId inside the headers

        List<PersonDto> personDtoList = connectionsServiceClient.getFirstDegreeConnections(userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found " + postId + "with ID " + postId));
        return modelMapper.map(post, PostDto.class);

    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all the posts of a user with ID: {}", userId);
        List<Post> postList = postRepository.findByUserId(userId);
        return postList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}