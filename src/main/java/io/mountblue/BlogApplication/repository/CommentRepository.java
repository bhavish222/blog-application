package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentById(Long id);
}
