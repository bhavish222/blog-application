package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.publishedAt DESC")
    List<Post> findPostsInAndOrderByPublishedAtDesc(
            @Param("posts") List<Post> posts
    );

    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.publishedAt ASC")
    List<Post> findPostsInAndOrderByPublishedAtAsc(
            @Param("posts") List<Post> posts
    );

    List<Post> findPostsByAuthorIn(
            List<User> userList
    );

    @Query("SELECT p FROM Post p WHERE p.publishedAt BETWEEN :startDate AND :endDate")
    List<Post> findPostsByPublishedAtDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Post> findAllPostsByIdIn(
            List<Long> postsIdList
    );
}