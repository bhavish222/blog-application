package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.ServiceImplementation;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController {
    public ServiceImplementation serviceImplementation;
    public CommentController(ServiceImplementation serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
    }
    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long id,
            @RequestParam(name = "commentId", required = false) Long commentId,
            @RequestParam(name = "commentname") String commentname,
            Model model
    ) {
        Post post = serviceImplementation.findPostById(id);
        List<Comment> listOfComment = post.getComments();
        Comment comment;
        if(commentId == null) {
            comment = new Comment();
        } else {
            comment = serviceImplementation.findCommentById(commentId);
        }
        comment.setComment(commentname);
        listOfComment.add(comment);
        post.setComments(listOfComment);
        comment.setPost(post);
        serviceImplementation.save(post);
        model.addAttribute("commentId",comment.getId());
        return "redirect:/post"+post.getId();
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

    @PostMapping("/updatecomment/comment{comment_id}")
    public String updateComment(
            @PathVariable("comment_id") Long id,
            Model model
    ) {
        Comment comment = serviceImplementation.findCommentById(id);
        Post post = comment.getPost();
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("commentId",comment.getId());
        model.addAttribute("commentname", comment.getComment());
        return "showSinglePost";
    }
}
