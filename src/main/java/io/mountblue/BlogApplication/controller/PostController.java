package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.ServiceImplementation;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

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
            @RequestParam("tagList") String tagsString,
            @RequestParam("action") String action
    ) {
        Post existingPost = serviceImplementation.findPostById(post.getId());
        String[] tagNames = tagsString.split(",");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            tags.add(tag);
        }
        post.setTags(tags);
        System.out.println(tags);
        if(action.equals("Publish")) {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        }
        else {
            post.setPublishedAt(existingPost.getPublishedAt());
            post.setCreatedAt(existingPost.getCreatedAt());
        }
        post.setUpdatedAt(LocalDateTime.now());
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
        StringBuilder tagListBuilder = new StringBuilder();
        List<Tag> tags = post.getTags();
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                if (tag != null) {
                    tagListBuilder.append(tag.getName());
                    if (i < tags.size() - 1) {
                        tagListBuilder.append(", ");
                    }
                }
            }
        }
        String tagsList = tagListBuilder.toString();
        model.addAttribute("post", post);
        model.addAttribute("tagsList", tagsList);
        return "newPost";
    }

    @PostMapping("/deletepost/post{post_id}")
    public String deletePost(
            @PathVariable("post_id") Long id
    ) {
        serviceImplementation.deletePostById(id);
        return "redirect:/";
    }

    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long id,
            @RequestParam("commentname") String commentname
    ) {
        Post post = serviceImplementation.findPostById(id);
        List<Comment> listOfComment = post.getComments();
        Comment comment = new Comment();
        comment.setComment(commentname);
        listOfComment.add(comment);
        post.setComments(listOfComment);
        comment.setPost(post);
        serviceImplementation.save(post);
        return "redirect:/post"+post.getId();
    }

    @PostMapping("/updatecomment/comment{comment_id}")
    public String updateComm0ent(
            @PathVariable("comment_id") Long id
    ) {
        Comment comment = serviceImplementation.findCommentById(id);

        return "";
    }

    @PostMapping("/deletecomment/comment{comment_id}")
    public String deleteComment(
            @PathVariable("comment_id") Long id
    ) {
        Comment comment = serviceImplementation.findCommentById(id);
        Post post = comment.getPost();
        serviceImplementation.deleteCommentById(id);
        return "redirect:/post"+post.getId();
    }
}