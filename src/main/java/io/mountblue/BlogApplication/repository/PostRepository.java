package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.publishedAt DESC")
    List<Post> findPostsInAndOrderByPublishedAtDesc(@Param("posts") List<Post> posts);

    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.publishedAt ASC")
    List<Post> findPostsInAndOrderByPublishedAtAsc(@Param("posts") List<Post> posts);
}