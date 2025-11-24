package com.vank.careerConnectPlatform.postsService.repository;

import com.vank.careerConnectPlatform.postsService.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);
}
