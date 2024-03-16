package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
    List<Post> findAllByOrderByPublishedAtDesc();
    List<Post> findAllByOrderByPublishedAtAsc();
}
