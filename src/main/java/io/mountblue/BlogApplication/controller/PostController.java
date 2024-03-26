package io.mountblue.BlogApplication.controller;

import org.springframework.data.domain.PageRequest;
import io.mountblue.BlogApplication.services.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;
    private PostTagService postTagService;
    private SearchAndSortService searchAndSortService;

    public PostController(
            PostService postService,
            TagService tagService,
            UserService userService,
            PostTagService postTagService,
            SearchAndSortService searchAndSortService
    ) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
        this.postTagService = postTagService;
        this.searchAndSortService = searchAndSortService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "searchBarInput", required = false) String searchBarInput,
            @RequestParam(name = "sort", defaultValue = "newest", required = false) String sort,
            @RequestParam(name = "tagId", required = false) List<Long> tagId,
            @RequestParam(name = "userId", required = false) List<Long> userId,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            Model model
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Post> searchedPosts = searchAndSortService.searchPostBySearchBarInput(searchBarInput);
        List<Post> filteredPosts = searchAndSortService.filteredByPosts(startDateStr, endDateStr, tagId, userId);
        List<Post> commonPosts = searchAndSortService.checkForSearchedAndFiltered(searchedPosts, filteredPosts);
        Page<Post> posts =  searchAndSortService.sort(commonPosts, sort, pageable);

        int totalPages = (int) Math.ceil((double) posts.getTotalElements() / (double) pageSize);
        boolean hasNextPages = pageNumber < totalPages - 1;
        model.addAttribute("hasNextPage", hasNextPages);
        model.addAttribute("pageNumber",pageNumber);

        List<Tag> tagList = tagService.findAllTags();
        List<User> userList = userService.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        model.addAttribute("posts", posts);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("tagId", tagId);
        model.addAttribute("sort", sort);
        model.addAttribute("userId", userId);
        model.addAttribute("searchBarInput", searchBarInput);
        model.addAttribute("pageNumber",pageNumber);
        return "landingPage";
    }

    @GetMapping("/newpost")
    public String newPost(
            Model model
    ) {
        model.addAttribute("post", new Post());
        return "newPost";
    }

    @PostMapping("/savepost")
    public String saveForm(
            @ModelAttribute("post") Post post,
            @RequestParam("tagList") String tagsString,
            @RequestParam("action") String action
    ) {
        postService.saveOrUpdate(post, tagsString, action);
        return "redirect:/post" + post.getId();
    }
    @GetMapping("/post{post_id}")
    public String showOnePost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = postService.findPostById(id);
        model.addAttribute("post",post);
        return "showSinglePost";
    }

    @PostMapping("/editpost/post{post_id}")
    public String editPost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = postService.findPostById(id);
        String tagsList = postService.findTagsOfPostToString(post);
        post.setUpdatedAt(LocalDateTime.now());
        model.addAttribute("post", post);
        model.addAttribute("tagsList", tagsList);
        return "newPost";
    }

    @PostMapping("/deletepost/post{post_id}")
    public String deletePost(
            @PathVariable("post_id") Long id
    ) {
        postService.deletePostById(id);
        return "redirect:/";
    }
}