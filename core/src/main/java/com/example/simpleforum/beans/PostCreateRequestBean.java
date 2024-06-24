package com.example.simpleforum.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreateRequestBean {
    private String title;

    private String content;
}
