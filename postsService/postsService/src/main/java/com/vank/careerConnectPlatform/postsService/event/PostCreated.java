package com.vank.careerConnectPlatform.postsService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreated {
    private Long postId;
    private Long userId;
    private String content;
}
