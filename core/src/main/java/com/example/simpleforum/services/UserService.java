package com.example.simpleforum.services;

import com.example.simpleforum.beans.PasswordEncoder;
import com.example.simpleforum.beans.UserCreateRequestBean;
import com.example.simpleforum.model.User;
import com.example.simpleforum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserCreateRequestBean createRequestBean) {
        User newUser = User.builder()
                .username(createRequestBean.getUsername())
                .password(passwordEncoder.encoder().encode(createRequestBean.getRawPassword()))
                .firstName(createRequestBean.getFirstName())
                .lastName(createRequestBean.getLastName())
                .build();
        return userRepository.saveAndFlush(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
