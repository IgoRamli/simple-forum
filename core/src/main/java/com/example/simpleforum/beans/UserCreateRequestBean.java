package com.example.simpleforum.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserCreateRequestBean {
    private String username;

    @JsonProperty("password")
    private String rawPassword;

    private String firstName;

    private String lastName;
}
