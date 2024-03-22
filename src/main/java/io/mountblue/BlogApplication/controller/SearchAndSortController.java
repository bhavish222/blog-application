package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchAndSortController {
    private SearchAndSortServiceImplementation searchAndSortServiceImplementation;
    private TagServiceImplementation tagServiceImplementation;
    private UserServiceImplementation userServiceImplementation;

    public SearchAndSortController(
            SearchAndSortServiceImplementation searchAndSortServiceImplementation,
            TagServiceImplementation tagServiceImplementation,
            UserServiceImplementation userServiceImplementation
    ) {
        this.searchAndSortServiceImplementation = searchAndSortServiceImplementation;
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
        List<Post> searchedPosts = searchAndSortServiceImplementation.searchPostBySearchBarInput(searchBarInput);
        List<Post> filteredPosts = searchAndSortServiceImplementation.filteredByPosts(startDateStr, endDateStr, tagId, userId);
        List<Post> commonPosts = searchAndSortServiceImplementation.checkForSearchedAndFiltered(searchedPosts, filteredPosts);
        commonPosts = searchAndSortServiceImplementation.sort(commonPosts, sort);

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