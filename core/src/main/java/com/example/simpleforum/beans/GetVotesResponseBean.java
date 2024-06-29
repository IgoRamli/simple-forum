package com.example.simpleforum.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetVotesResponseBean {
    private long postId;
    private long upvotes;
    private long downvotes;
    private Boolean isUpvoted;
    private Boolean isDownvoted;
}
