package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;

import java.util.List;

public interface SearchAndSortService {
    List<Post> toFindAllPostsForSearch(List<Post> allPostsList, String searchBarInput);
    List<Post> combineFilters(List<Post> postsForTags, List<Post> postsForUser, List<Post> postsForDate );
}