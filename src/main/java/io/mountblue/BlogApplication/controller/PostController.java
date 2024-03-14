package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.ServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private ServiceImplementation serviceImplementation;

    public PostController(ServiceImplementation serviceImplementation){
        this.serviceImplementation = serviceImplementation;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = serviceImplementation.showAllPosts();
        model.addAttribute("posts", posts);
        return "landingPage";
    }

    @GetMapping("/newpost")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "newPost";
    }

    @PostMapping("/savepost")
    public String saveForm(
            @ModelAttribute("post") Post post,
            @RequestParam("tagList") String tagsString
    ) {
        String[] tagNames = tagsString.split(",");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            tags.add(tag);
        }
        post.setTags(tags);
        serviceImplementation.save(post);
        return "redirect:/";
    }

    @GetMapping("/post{post_id}")
    public String showOnePost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = serviceImplementation.findPostById(id);
        model.addAttribute("post",post);
        return "showSinglePost";
    }

    @GetMapping("/editpost/post{post_id}")
    public String editPost(
            @PathVariable("post_id") Long id,
            Model model
    ) {
        Post post = serviceImplementation.findPostById(id);
        model.addAttribute("post", post);
        return "newPost";
    }
}