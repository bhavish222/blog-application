package io.mountblue.BlogApplication.restcontroller;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class PostRestController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;
    private PostTagService postTagService;
    private SearchAndSortService searchAndSortService;

    public PostRestController(
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
    public List<Post> index(
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
        return commonPosts;
    }

    @PostMapping("/savepost")
    public String saveForm(@RequestBody Post post
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findUserByName(loggedInUser);
        post.setAuthor(user);
        postService.save(post);
        return "post saved successfully";
    }

    @GetMapping("/post{post_id}")
    public Post showOnePost(
            @PathVariable("post_id") Long id
    ) {
        return postService.findPostById(id);
    }

    @PutMapping("/editpost/post{post_id}")
    public String editPost(
            @PathVariable("post_id") Long id,
            @RequestBody Post updatedPost
    ) {
        return postService.updatePostByIdForRest(id, updatedPost);
    }

    @DeleteMapping("/deletepost/post{post_id}")
    public String deletePost(
            @PathVariable("post_id") Long id
    ) {
        return postService.deletePostByIdForRest(id);
    }
}