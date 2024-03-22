package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchAndSortService {
    List<Post> toFindAllPostsForSearch(List<Post> allPostsList, String searchBarInput);
    List<Post> combineFilters(List<Post> postsForTags, List<Post> postsForUser, List<Post> postsForDate);

    List<Post> searchPostBySearchBarInput(String searchBarInput);

    List<Post> filteredByPosts(String startDateStr, String endDateStr, List<Long> tagId, List<Long> userId);

    List<Post> checkForSearchedAndFiltered(List<Post> searchedPosts, List<Post> filteredPosts);

    Page<Post> sort(List<Post> commonPosts, String sort, Pageable pageable);
}