package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Post> posts = new ArrayList<>();
        if(!postsForTags.isEmpty() && !postsForUser.isEmpty()) {
            for (Post post : postsForTags) {
                if (postsForUser.contains(post)) {
                    posts.add(post);
                }
            }
        }
        else if(postsForTags.isEmpty() && !postsForUser.isEmpty()) {
            posts = postsForUser;
        }
        else if(!postsForTags.isEmpty()) {
            posts = postsForTags;
        }
        else {
            posts = postServiceImplementation.findAllPosts();
        }

        List<Post> allUniquePosts = new ArrayList<>();

        if(!postsForDate.isEmpty()) {
            for(Post post : posts) {
                if(postsForDate.contains(post)) {
                    allUniquePosts.add(post);
                }
            }
        }
        else {
            allUniquePosts = posts;
        }
        return allUniquePosts;
    }
}