package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Service
public class SearchAndSortServiceImplementation implements SearchAndSortService {
    public SearchAndSortServiceImplementation() {}
    private PostServiceImplementation postServiceImplementation;
    private PostTagServiceImplementation postTagServiceImplementation;

    public SearchAndSortServiceImplementation(
            PostServiceImplementation postServiceImplementation,
            PostTagServiceImplementation postTagServiceImplementation
    ) {
        this.postServiceImplementation = postServiceImplementation;
        this.postTagServiceImplementation = postTagServiceImplementation;
    }

    @Override
    public List<Post> toFindAllPostsForSearch(List<Post> allPostsList, String searchBarInput) {
        List<Post> filteredPostBasedOnSearch = new ArrayList<>();
        for(Post post : allPostsList) {
            if(post.getTitle().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            if(post.getContent().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            for(Tag tag : post.getTags()) {
                if(tag.getName().toLowerCase().contains(searchBarInput.toLowerCase())) {
                    filteredPostBasedOnSearch.add(post);
                    break;
                }
            }
        }
        return filteredPostBasedOnSearch;
    }

    @Override
    public List<Post> combineFilters(List<Post> postsForTags, List<Post> postsForUser, List<Post> postsForDate) {
        Set<Post> uniquePosts = new HashSet<>();
        uniquePosts.addAll(postsForTags);
        uniquePosts.addAll(postsForUser);
        uniquePosts.addAll(postsForDate);
        List<Post> posts = new ArrayList<>(uniquePosts);
        return posts;
    }
}