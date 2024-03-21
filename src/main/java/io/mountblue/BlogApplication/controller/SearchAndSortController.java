package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
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

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "searchBarInput", required = false) String searchBarInput,
            @RequestParam(value = "sort", defaultValue = "newest") String sort,
            Model model
    ) {
        List<Post> posts = postServiceImplementation.findAllPosts();
        List<Post> filteredPostBasedOnSearch;
        if (searchBarInput != null && !searchBarInput.isEmpty()) {
            filteredPostBasedOnSearch = searchAndSortServiceImplementation.toFindAllPostsForSearch(posts, searchBarInput);
        } else {
            filteredPostBasedOnSearch = postServiceImplementation.findAllPosts();
        }

        filteredPostBasedOnSearch = postServiceImplementation.getPostsSortedByDate(filteredPostBasedOnSearch);
        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        model.addAttribute("posts", filteredPostBasedOnSearch);
        model.addAttribute("searchBarInput", searchBarInput);
        model.addAttribute("sort", sort);
        return "landingPage";
    }

    @GetMapping("/sort")
    public String index(
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            @RequestParam("searchBarInput") String searchBarInput,
            @RequestParam(name = "postId") List<Long> postsIdList,
            Model model
    ) {
        List<Post> posts = postServiceImplementation.findAllPostsByIdIn(postsIdList);
        List<Post> filteredPost = searchAndSortServiceImplementation.toFindAllPostsForSearch(posts, searchBarInput);
        if (sort.equals("newest")) {
            filteredPost = postServiceImplementation.getPostsSortedByDate(filteredPost);
        } else {
            filteredPost = postServiceImplementation.getPostsSortedByOldestDate(filteredPost);
        }

        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();

        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        model.addAttribute("posts", filteredPost);
        model.addAttribute("searchBarInput", searchBarInput);
        return "landingPage";
    }

    @GetMapping("/filter-tags")
    public String filterTags(
            @RequestParam(name = "tagId", required = false) List<Tag> tagIds,
            @RequestParam(name = "userId", required = false) List<User> userIds,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(name = "postId") List<Long> postsIdList,
            Model model
    ) {
        List<Post> postsForTags = new ArrayList<>();
        List<Post> postsForUser = new ArrayList<>();
        List<Post> postsForDate = new ArrayList<>();
        if (tagIds != null) {
            postsForTags = postTagServiceImplementation.findAllPostsByTags(tagIds);
        }
        if (userIds != null) {
            postsForUser = postServiceImplementation.findPostsByAuthorIn(userIds);
        }
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            postsForDate = postServiceImplementation.findPostsByPublishedAtDateRange(startDateStr, endDateStr);
        }

        List<Post> posts = searchAndSortServiceImplementation.combineFilters(postsForTags, postsForUser, postsForDate);
        List<Post> existingPostsInPage = postServiceImplementation.findAllPostsByIdIn(postsIdList);
        List<Post> commonPosts = new ArrayList<>();
        for (Post post : posts) {
            if (existingPostsInPage.contains(post)) {
                commonPosts.add(post);
            }
        }
        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        commonPosts = postServiceImplementation.getPostsSortedByDate(commonPosts);
        model.addAttribute("posts", commonPosts);
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        return "landingPage";
    }
}