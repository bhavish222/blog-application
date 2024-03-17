package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;

public interface CommentService {
    Comment findCommentById(Long id);
    void deleteCommentById(Long id);
    Long setCommentsForPost(Post post, Long commentId, String commentName);
}
