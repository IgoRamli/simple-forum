package com.example.simpleforum.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "posts")
@Data
@EqualsAndHashCode(callSuper = true)
public class Post extends CreateUpdateDeleteTimestampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", nullable = false)
    private String content;
}
