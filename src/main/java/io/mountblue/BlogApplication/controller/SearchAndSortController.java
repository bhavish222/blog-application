package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchAndSortController {
    private PostServiceImplementation postServiceImplementation;

    public SearchAndSortController(PostServiceImplementation postServiceImplementation) {
        this.postServiceImplementation = postServiceImplementation;
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "searchBarInput") String searchBarInput,
            Model model
    ) {
        List<Post> allPostsList = postServiceImplementation.findAllPosts();
        List<Post> filteredPostBasedOnSearch = new ArrayList<>();
        toFindAllPostsForSearch(allPostsList, filteredPostBasedOnSearch, searchBarInput);
        model.addAttribute("posts", filteredPostBasedOnSearch);
        return "landingPage";
    }

//    private String resettingSorting(Model model) {
//        return "";
//    }

    private void toFindAllPostsForSearch(List<Post> allPostsList, List<Post> filteredPostBasedOnSearch, String searchBarInput) {
        for(Post post : allPostsList) {
            if(post.getTitle().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            else if(post.getContent().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            else {
                for(Tag tag : post.getTags()) {
                    if(tag.getName().toLowerCase().contains(searchBarInput.toLowerCase())) {
                        filteredPostBasedOnSearch.add(post);
                        break;
                    }
                }
            }
        }
    }

    @GetMapping("/sort")
    public String index(
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            Model model
    ) {

        List<Post> posts;
        if (sort.equals("newest")) {
            posts = postServiceImplementation.getAllPostsSortedByDate();
        } else {
            posts = postServiceImplementation.getAllPostsSortedByOldestDate();
        }
        model.addAttribute("posts", posts);
        return "landingPage";
    }

}