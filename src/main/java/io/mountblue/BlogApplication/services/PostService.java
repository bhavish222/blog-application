package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import java.util.List;

public interface PostService {
    void save(Post post);
    List<Post> findAllPosts();
    Post findPostById(Long id);
    void deletePostById(Long id);
    List<Post> getPostsSortedByDate(List<Post> post);
    List<Post> getPostsSortedByOldestDate(List<Post> post);
    String findTagsOfPostToString(Post post);
    void saveOrUpdate(Post post, String tagsString, String action);
    List<Post> findPostsByAuthorIn(List<User> userList);
    List<Post> findPostsByPublishedAtDateRange(String startDate, String endDate);
    List<Post> findAllPostsByIdIn(List<Long> postsIdList);
}