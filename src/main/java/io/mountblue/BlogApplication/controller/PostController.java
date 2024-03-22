package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    private PostServiceImplementation postServiceImplementation;
    private TagServiceImplementation tagServiceImplementation;
    private UserServiceImplementation userServiceImplementation;
    private PostTagServiceImplementation postTagServiceImplementation;
    private SearchAndSortServiceImplementation searchAndSortServiceImplementation;



    public PostController(
            PostServiceImplementation postServiceImplementation,
            TagServiceImplementation tagServiceImplementation,
            UserServiceImplementation userServiceImplementation,
            PostTagServiceImplementation postTagServiceImplementation,
            SearchAndSortServiceImplementation searchAndSortServiceImplementation
    ) {
        this.postServiceImplementation = postServiceImplementation;
        this.tagServiceImplementation = tagServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
        this.postTagServiceImplementation = postTagServiceImplementation;
        this.searchAndSortServiceImplementation = searchAndSortServiceImplementation;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "searchBarInput", required = false) String searchBarInput,
            @RequestParam(value = "sort", defaultValue = "newest", required = false) String sort,
            @RequestParam(name = "tagId", required = false) List<Long> tagId,
            @RequestParam(name = "userId", required = false) List<Long> userId,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            Model model
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Post> searchedPosts = searchAndSortServiceImplementation.searchPostBySearchBarInput(searchBarInput);
        List<Post> filteredPosts = searchAndSortServiceImplementation.filteredByPosts(startDateStr, endDateStr, tagId, userId);
        List<Post> commonPosts = searchAndSortServiceImplementation.checkForSearchedAndFiltered(searchedPosts, filteredPosts);
        Page<Post> posts =  searchAndSortServiceImplementation.sort(commonPosts, sort, pageable);

        int totalPages = (int) (posts.getTotalElements() / pageSize);
        boolean hasNextPages = pageNumber < totalPages - 1;

        model.addAttribute("hasNextPage", hasNextPages);
        model.addAttribute("pageNumber",pageNumber);

        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        model.addAttribute("posts", posts);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("tagId", tagId);
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
        postServiceImplementation.saveOrUpdate(post, tagsString, action);
        postServiceImplementation.save(post);
        return "redirect:/post"+post.getId();
    }
    @GetMapping("/post{post_id}")
    public String showOnePost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = postServiceImplementation.findPostById(id);
        model.addAttribute("post",post);
        return "showSinglePost";
    }

    @PostMapping("/editpost/post{post_id}")
    public String editPost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = postServiceImplementation.findPostById(id);
        String tagsList = postServiceImplementation.findTagsOfPostToString(post);
        post.setUpdatedAt(LocalDateTime.now());
        model.addAttribute("post", post);
        model.addAttribute("tagsList", tagsList);
        return "newPost";
    }

    @PostMapping("/deletepost/post{post_id}")
    public String deletePost(
            @PathVariable("post_id") Long id
    ) {
        postServiceImplementation.deletePostById(id);
        return "redirect:/";
    }
}