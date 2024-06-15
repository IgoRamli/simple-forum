package com.example.simpleforum.repositories;

import com.example.simpleforum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<Post> findAll();

    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL AND p.title LIKE CONCAT('%', ?1, '%') ORDER BY p.createdAt DESC")
    List<Post> findByTitleKeyword(String keyword);

    @Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.deletedAt IS NULL")
    Optional<Post> findById(Long id);

    @Query("UPDATE Post p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = ?1")
    @Modifying
    void deleteById(Long id);
}
