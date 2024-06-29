package com.example.simpleforum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(VoteId.class)
@Table(name = "votes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Vote {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "type")
    private String type;
}
