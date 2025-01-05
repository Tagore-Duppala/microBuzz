package com.microBuzz.post_service.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostLikedEvent {

    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
