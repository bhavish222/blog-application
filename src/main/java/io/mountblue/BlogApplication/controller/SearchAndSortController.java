package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchAndSortController {
    private PostServiceImplementation postServiceImplementation;
    private SearchAndSortServiceImplementation searchAndSortServiceImplementation;
    private PostTagServiceImplementation postTagServiceImplementation;
    private TagServiceImplementation tagServiceImplementation;
    private UserServiceImplementation userServiceImplementation;

    public SearchAndSortController(
            PostServiceImplementation postServiceImplementation,
            SearchAndSortServiceImplementation searchAndSortServiceImplementation,
            PostTagServiceImplementation postTagServiceImplementation,
            TagServiceImplementation tagServiceImplementation,
            UserServiceImplementation userServiceImplementation
    ) {
        this.postServiceImplementation = postServiceImplementation;
        this.searchAndSortServiceImplementation = searchAndSortServiceImplementation;
        this.postTagServiceImplementation = postTagServiceImplementation;
        this.tagServiceImplementation = tagServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/search/filters/sort")
    public String searchFiltersSort(
            @RequestParam(name = "searchBarInput", required = false) String searchBarInput,
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            @RequestParam(name = "tagId", required = false) List<Long> tagId,
            @RequestParam(name = "userId", required = false) List<Long> userId,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            Model model
    ) {
        List<Post> searchedPosts = postServiceImplementation.findAllPosts();
        if(searchBarInput != null && !searchBarInput.isEmpty()) {
            searchedPosts = searchAndSortServiceImplementation.toFindAllPostsForSearch(searchedPosts, searchBarInput);
        }
        else {
            searchedPosts = postServiceImplementation.findAllPosts();
        }

        if(startDateStr == null) {
            startDateStr = "";
            endDateStr = "";
        }
        List<Post> postsForTags = new ArrayList<>();
        List<Post> postsForUser = new ArrayList<>();
        List<Post> postsForDate = new ArrayList<>();

        if(tagId != null) {
            postsForTags = postTagServiceImplementation.findAllPostsByTags(tagId);
        }
        if(userId != null) {
            postsForUser = postServiceImplementation.findPostsByAuthorIn(userId);
        }
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            postsForDate = postServiceImplementation.findPostsByPublishedAtDateRange(startDateStr, endDateStr);
        }

        List<Post> unionPosts = searchAndSortServiceImplementation.combineFilters(postsForTags, postsForUser, postsForDate);
        List<Post> commonPosts = new    ArrayList<>();
        if(!unionPosts.isEmpty()) {
            for(Post post : searchedPosts) {
                if(unionPosts.contains(post)) {
                    commonPosts.add(post);
                }
            }
        }
        else {
            commonPosts = searchedPosts;
        }

        if(sort.equals("newest")) {
            commonPosts = postServiceImplementation.getPostsSortedByDate(commonPosts);
        }
        else {
            commonPosts = postServiceImplementation.getPostsSortedByOldestDate(commonPosts);
        }

        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        model.addAttribute("posts", commonPosts);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("tagId", tagId);
        model.addAttribute("userId", userId);
        model.addAttribute("searchBarInput", searchBarInput);
        return "landingPage";

    }
}