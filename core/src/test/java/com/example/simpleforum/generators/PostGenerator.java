package com.example.simpleforum.generators;

import com.example.simpleforum.model.Post;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostGenerator {
    public static Post generateRandomPost(Long id, String userId) {
        Post post = new Post();
        post.setId(id);
        post.setTitle("Random Title");
        post.setContent("Random content");
        post.setUserId(userId);
        return post;
    }

    public static Post generateRandomPost(Long id) {
        return generateRandomPost(id, UUID.randomUUID().toString());
    }

    public static Post generateRandomPost() {
        return generateRandomPost(new Random().nextLong());
    }

    public static List<Post> generateRandomListOfPosts(int length) {
        return Stream.generate(PostGenerator::generateRandomPost)
                .limit(length)
                .collect(Collectors.toList());
    }
}
