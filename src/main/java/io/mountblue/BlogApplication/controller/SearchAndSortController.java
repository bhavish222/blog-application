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

    public SearchAndSortController(PostServiceImplementation postServiceImplementation, SearchAndSortServiceImplementation searchAndSortServiceImplementation, PostTagServiceImplementation postTagServiceImplementation, TagServiceImplementation tagServiceImplementation, UserServiceImplementation userServiceImplementation) {
        this.postServiceImplementation = postServiceImplementation;
        this.searchAndSortServiceImplementation = searchAndSortServiceImplementation;
        this.postTagServiceImplementation = postTagServiceImplementation;
        this.tagServiceImplementation = tagServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
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
            Model model
    ) {
        List<Post> posts = postServiceImplementation.findAllPosts();
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
            @RequestParam(value = "sort" ,defaultValue = "newest") String sort,
            Model model
    ) {
        List<Post> postsForTags = new ArrayList<>();
        List<Post> postsForUser = new ArrayList<>();
        List<Post> postsForDate = new ArrayList<>();
        if(tagIds != null) {
            postsForTags = postTagServiceImplementation.findAllPostsByTags(tagIds);
        }
        if(userIds != null) {
            postsForUser = postServiceImplementation.findPostsByAuthorIn(userIds);
        }
        if(!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            postsForDate = postServiceImplementation.findPostsByPublishedAtDateRange(startDateStr, endDateStr);
        }
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

        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);

        allUniquePosts = postServiceImplementation.getPostsSortedByDate(allUniquePosts);
        model.addAttribute("posts", allUniquePosts);
        return "landingPage";
    }
}