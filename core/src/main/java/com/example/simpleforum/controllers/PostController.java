package com.example.simpleforum.controllers;

import com.example.simpleforum.beans.AuthUserBean;
import com.example.simpleforum.beans.PostCreateRequestBean;
import com.example.simpleforum.exceptions.DataNotFoundException;
import com.example.simpleforum.model.Post;
import com.example.simpleforum.repositories.PostRepository;
import com.example.simpleforum.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PostController {
    private static final Long BATCH_LIMIT = 100L;

    private final PostRepository postRepository;

    private final AuthUtils authUtils;

    private ModelMapper modelMapper;

    @Autowired
    public PostController(PostRepository postRepository, AuthUtils authUtils, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.authUtils = authUtils;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String healthCheck() {
        return "Welcome to Simple Forum!";
    }

    @GetMapping("/posts")
    public List<Post> getPosts(@RequestParam Optional<String> keyword, @RequestParam Long limit, @RequestParam Optional<Long> lastId) {
        if (keyword.isPresent()) {
            return lastId.map(id -> postRepository.findByTitleKeyword(keyword.get(), limit, id))
                                    .orElseGet(() -> postRepository.findByTitleKeyword(keyword.get(), limit));
        } else {
            return lastId.map(id -> postRepository.findAll(limit, id))
                                    .orElseGet(() -> postRepository.findAll(limit));
        }
    }

    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new DataNotFoundException(String.format("Post with id '%d' not found", id)));
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody PostCreateRequestBean postCreateRequest, Authentication authentication) {
        AuthUserBean user = authUtils.getAuthDetails(authentication);
        Post newPost = modelMapper.map(postCreateRequest, Post.class);
        newPost.setUserId(user.getId());
        return postRepository.saveAndFlush(newPost);
    }

//    @PutMapping("/posts/{id}")
//    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
//        Optional<Post> currentPostOpt = postRepository.findById(id);
//        if (currentPostOpt.isEmpty()) {
//            throw new DataNotFoundException(String.format("Post with id '%d' not found", id));
//        }
//        Post currentPost = currentPostOpt.get();
//        currentPost.setContent(post.getContent());
//        postRepository.save(currentPost, id);
//        return postRepository.saveAndFlush(currentPost);
//    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        Optional<Post> currentPostOpt = postRepository.findById(id);
        if (currentPostOpt.isEmpty()) {
            throw new DataNotFoundException(String.format("Post with id '%d' not found", id));
        }
        postRepository.deleteById(id);
    }
}
