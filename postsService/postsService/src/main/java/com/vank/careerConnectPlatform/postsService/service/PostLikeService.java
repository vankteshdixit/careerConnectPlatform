package com.vank.careerConnectPlatform.postsService.service;

import com.vank.careerConnectPlatform.postsService.entity.PostLike;
import com.vank.careerConnectPlatform.postsService.exception.BadRequestException;
import com.vank.careerConnectPlatform.postsService.exception.ResourceNotFoundException;
import com.vank.careerConnectPlatform.postsService.repository.PostLikeRepository;
import com.vank.careerConnectPlatform.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void likePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} liking the post with ID: {}", userId, postId);

        postRepository.findById(postId)
                .orElseThrow(()
                        ->new ResourceNotFoundException("Post not found with ID: " +postId));

        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(hasAlreadyLiked) throw new BadRequestException("You Can't like the post again");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

//        TODO: Send notification to the owner of the post

    }

    @Transactional
    public void unlikePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} unliking the post with ID: {}", userId, postId);

        postRepository.findById(postId)
                .orElseThrow(()
                        ->new ResourceNotFoundException("Post not found with ID: " +postId));

        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!hasAlreadyLiked) throw new BadRequestException("You Can't unlike the post that you have not liked");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
