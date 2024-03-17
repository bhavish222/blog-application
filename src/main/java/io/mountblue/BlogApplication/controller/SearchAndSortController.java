package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.services.SearchAndSortServiceImplementation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(name = "searchBarInput") String searchBarInput,
            Model model
    ) {
        List<Post> allPostsList = postServiceImplementation.findAllPosts();
        List<Post> filteredPostBasedOnSearch = searchAndSortServiceImplementation.toFindAllPostsForSearch(allPostsList, searchBarInput);
        filteredPostBasedOnSearch = postServiceImplementation.getPostsSortedByDate(filteredPostBasedOnSearch);
        model.addAttribute("posts", filteredPostBasedOnSearch);
        return "landingPage";
    }


    @GetMapping("/sort")
    public String index(
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            Model model
    ) {

        List<Post> posts = postServiceImplementation.findAllPosts();
        if (sort.equals("newest")) {
            posts = postServiceImplementation.getPostsSortedByDate(posts);
        } else {
            posts = postServiceImplementation.getPostsSortedByOldestDate(posts);
        }
        model.addAttribute("posts", posts);
        return "landingPage";
    }
}