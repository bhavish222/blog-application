package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.ServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
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
        List<Post> posts = serviceImplementation.showPosts();
        model.addAttribute("posts", posts);
        return "landingPage";
    }

    @GetMapping("/newpost")
    public String newPost(Model model) {
        model.addAttribute("form", new Post());
        return "newPost";
    }

    @PostMapping("/action")
    public String saveForm(@ModelAttribute("form") Post post, @RequestParam String action, @RequestParam("tagList") String tagsString) {
        if ("Publish".equals(action)) {
            Post existingPost = serviceImplementation.findPostByTitleAndContent(post.getTitle(), post.getContent());
            if (existingPost != null) {
                serviceImplementation.publish(existingPost.getId(), true);
                return "landingPage";
            }
            // If the post doesn't exist, proceed to publish the submitted post
            post.setIs_published(true);
            post.setPublished_at(LocalDateTime.now());
        }
        else if ("Save".equals(action)) {
            post.setIs_published(false);
        }
        String[] tagNames = tagsString.split(",");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tags.add(tag);
        }
        post.setTags(tags);
        serviceImplementation.save(post);
        return "redirect:/";
    }

}
