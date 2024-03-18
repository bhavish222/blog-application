package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.services.SearchAndSortServiceImplementation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchAndSortController {
    private PostServiceImplementation postServiceImplementation;
    private SearchAndSortServiceImplementation searchAndSortServiceImplementation;

    public SearchAndSortController(PostServiceImplementation postServiceImplementation, SearchAndSortServiceImplementation searchAndSortServiceImplementation) {
        this.postServiceImplementation = postServiceImplementation;
        this.searchAndSortServiceImplementation = searchAndSortServiceImplementation;
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "searchBarInput", required = false) String searchBarInput,
            Model model,
            @RequestParam(value = "sort" ,defaultValue = "newest") String sort
    ) {
        List<Post> posts = postServiceImplementation.findAllPosts();
        List<Post> filteredPostBasedOnSearch;
        if(searchBarInput != null && !searchBarInput.isEmpty()) {
            filteredPostBasedOnSearch = searchAndSortServiceImplementation.toFindAllPostsForSearch(posts, searchBarInput);
        }
        else {
            filteredPostBasedOnSearch = postServiceImplementation.findAllPosts();
        }

        filteredPostBasedOnSearch = postServiceImplementation.getPostsSortedByDate(filteredPostBasedOnSearch);
        model.addAttribute("posts", filteredPostBasedOnSearch);
        model.addAttribute("searchBarInput", searchBarInput);
        model.addAttribute("sort", sort);
        return "landingPage";
    }

    @GetMapping("/sort")
    public String index(
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            @RequestParam("searchBarInput") String searchBarInput,
            Model model
    ) {
        List<Post> posts = postServiceImplementation.findAllPosts();
        List<Post> filteredPost = searchAndSortServiceImplementation.toFindAllPostsForSearch(posts, searchBarInput);
        if (sort.equals("newest")) {
            filteredPost = postServiceImplementation.getPostsSortedByDate(filteredPost);
        } else {
            filteredPost = postServiceImplementation.getPostsSortedByOldestDate(filteredPost);
        }
        model.addAttribute("posts", filteredPost);
        model.addAttribute("searchBarInput", searchBarInput);
        return "landingPage";
    }

//    @GetMapping("/filter-tags")
//    public String filterTags(
//            @RequestParam(name = "tagId") List<Long> tagIds
//    ) {
////        List<Post> posts = ;
//        return "landingPage";
//    }

}