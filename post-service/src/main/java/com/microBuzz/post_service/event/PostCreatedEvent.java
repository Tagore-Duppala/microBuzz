package com.microBuzz.post_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {

    private Long creatorId;
    private String content;
    private Long postId;

}
