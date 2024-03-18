package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.services.TagServiceImplementation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private PostServiceImplementation postServiceImplementation;
    private TagServiceImplementation tagServiceImplementation;

    public PostController(PostServiceImplementation postServiceImplementation, TagServiceImplementation tagServiceImplementation){
        this.postServiceImplementation = postServiceImplementation;
        this.tagServiceImplementation = tagServiceImplementation;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postServiceImplementation.findAllPosts();
        posts = postServiceImplementation.getPostsSortedByDate(posts);
        List<Tag> tagList = tagServiceImplementation.findAllTags();
        model.addAttribute("posts", posts);
        model.addAttribute("tagList", tagList);
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
        List<Tag> tags= new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            tags.add(tag);
        }
        if (existingPost != null) {
            tags.addAll(existingPost.getTags());
            post.setPublishedAt(existingPost.getPublishedAt());
            post.setCreatedAt(existingPost.getCreatedAt());
        } else {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        }
        int currentPostLength = post.getContent().length();
        String excerpt = post.getContent().substring(0, Math.min(currentPostLength, 150));
        post.setExcerpt(excerpt);
        post.setTags(new ArrayList<>(tags));
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