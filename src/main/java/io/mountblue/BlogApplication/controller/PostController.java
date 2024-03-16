package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;

@Controller
public class PostController {

    private PostServiceImplementation postServiceImplementation;

    public PostController(PostServiceImplementation postServiceImplementation){
        this.postServiceImplementation = postServiceImplementation;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postServiceImplementation.getAllPostsSortedByDate();
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
        post.setIs_published(true);
        Post existingPost = postServiceImplementation.findPostById(post.getId());
        String[] tagNames = tagsString.split(",");
        LinkedHashSet<Tag> uniqueTags = new LinkedHashSet<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            uniqueTags.add(tag);
        }
        if (existingPost != null) {
            uniqueTags.addAll(existingPost.getTags());
            post.setPublishedAt(existingPost.getPublishedAt());
            post.setCreatedAt(existingPost.getCreatedAt());
        } else {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        }

        post.setTags(new ArrayList<>(uniqueTags));
        post.setUpdatedAt(LocalDateTime.now());

        postServiceImplementation.save(post);
        return "redirect:/";
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
        List<Tag> listOfTags = post.getTags();
        StringBuilder tagListBuilder = new StringBuilder();
        List<Tag> tags = post.getTags();
        if (tags != null) {
            for(Tag tag : tags) {
                tagListBuilder.append(tag.getName()).append(",");
            }
        }
        String tagsList = tagListBuilder.toString();
        post.setUpdatedAt(LocalDateTime.now());
        model.addAttribute("post", post);
        model.addAttribute("tagsList", tagsList);
        System.out.println(tagsList);
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
