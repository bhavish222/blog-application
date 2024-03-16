package io.mountblue.BlogApplication.dao;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;

import java.util.List;

public interface PostService {

    void save(Post post);
    List<Post> findAllPosts();
    Post findPostById(Long id);
    void deletePostById(Long id);
    Comment findCommentById(Long id);
    void deleteCommentById(Long id);
    List<Post> getAllPostsSortedByDate();
    List<Post> getAllPostsSortedByOldestDate();

}
