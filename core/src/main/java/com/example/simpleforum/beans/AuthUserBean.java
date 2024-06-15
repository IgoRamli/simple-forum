package com.example.simpleforum.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthUserBean {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
