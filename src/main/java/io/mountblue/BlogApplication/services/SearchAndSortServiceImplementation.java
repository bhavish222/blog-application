package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.services.PostServiceImplementation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchAndSortServiceImplementation implements SearchAndSortService {
    public SearchAndSortServiceImplementation() {}
    private PostServiceImplementation postServiceImplementation;
    public SearchAndSortServiceImplementation(PostServiceImplementation postServiceImplementation){
        this.postServiceImplementation = postServiceImplementation;
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
}