package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostService {

    void save(Post post);
    List<Post> findAllPosts();
    Post findPostById(Long id);
    void deletePostById(Long id);
    List<Post> getPostsSortedByDate(List<Post> post);
    List<Post> getPostsSortedByOldestDate(List<Post> post);
    String findTagsOfPostToString(Post post);
    void saveOrUpdate(Post post, String tagsString);
    List<Post> findPostsByAuthorIn(List<User> userList);
    List<Post> findPostsByPublishedAtDateRange(String startDate, String endDate);
    List<Post> findAllPostsByIdsIn(List<Long> postsIdList);
}
