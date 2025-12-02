package com.vank.careerConnectPlatform.postsService.service;

import com.vank.careerConnectPlatform.postsService.auth.AuthContextHolder;
import com.vank.careerConnectPlatform.postsService.client.ConnectionsServiceClient;
import com.vank.careerConnectPlatform.postsService.client.UploaderServiceClient;
import com.vank.careerConnectPlatform.postsService.dto.PersonDto;
import com.vank.careerConnectPlatform.postsService.dto.PostCreateRequestDto;
import com.vank.careerConnectPlatform.postsService.dto.PostDto;
import com.vank.careerConnectPlatform.postsService.entity.Post;
import com.vank.careerConnectPlatform.postsService.event.PostCreated;
import com.vank.careerConnectPlatform.postsService.exception.ResourceNotFoundException;
import com.vank.careerConnectPlatform.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsServiceClient connectionsServiceClient;
    private final KafkaTemplate<Long, PostCreated> postCreatedKafkaTemplate;
    private final UploaderServiceClient uploaderServiceClient;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, MultipartFile file) {
        Long userId = AuthContextHolder.getCurrentUserId();
        log.info("Creating post for user with id: {}", userId);

        ResponseEntity<String> imageUrl = uploaderServiceClient.uploadFIle(file);

        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post.setImageUrl(imageUrl.getBody());
        post = postRepository.save(post);

        List<PersonDto> personDtoList = connectionsServiceClient.getFirstDegreeConnections(userId);

        for(PersonDto person : personDtoList){ // send notification to each connection
            PostCreated postCreated = PostCreated.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .userId(person.getUserId())
                    .ownerUserId(userId)
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic", postCreated);
        }

        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);

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