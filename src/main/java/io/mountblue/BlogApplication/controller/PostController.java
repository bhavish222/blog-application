package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.PostTagServiceImplementation;
import io.mountblue.BlogApplication.services.TagServiceImplementation;
import io.mountblue.BlogApplication.services.UserServiceImplementation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    public PostController(
            PostServiceImplementation postServiceImplementation,
            TagServiceImplementation tagServiceImplementation,
            UserServiceImplementation userServiceImplementation,
            PostTagServiceImplementation postTagServiceImplementation
    ){
        this.postServiceImplementation = postServiceImplementation;
        this.tagServiceImplementation = tagServiceImplementation;
        this.userServiceImplementation = userServiceImplementation;
        this.postTagServiceImplementation = postTagServiceImplementation;
    }

    @GetMapping("/pageNo{pageNo}/pageSize{pageSize}")
    public String findPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @PathVariable(value = "pageSize") int pageSize,
            Model model
    ) {
        Page<Post> page = postServiceImplementation.paginationPage(pageNo, pageSize);
        List<Post> posts = page.getContent();
        posts = postServiceImplementation.getPostsSortedByDate(posts);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("posts", posts);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalPosts", page.getTotalElements());
        List<Tag> tagList = tagServiceImplementation.findAllTags();
        List<User> userList = userServiceImplementation.findAllUsers();
        model.addAttribute("tagList", tagList);
        model.addAttribute("userList", userList);
        return "landingPage";
    }

    @GetMapping("/")
    public String index(
            Model model
    ) {
        return findPaginated(1,3, model);
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