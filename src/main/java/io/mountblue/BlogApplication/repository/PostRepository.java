package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
}
