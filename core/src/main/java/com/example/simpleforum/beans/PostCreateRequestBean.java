package com.example.simpleforum.beans;

import lombok.Data;

@Data
public class PostCreateRequestBean {
    private String title;

    private String content;

    private Long userId;
}
