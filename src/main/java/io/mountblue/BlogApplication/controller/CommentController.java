package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.CommentServiceImplementation;
import io.mountblue.BlogApplication.services.PostServiceImplementation;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    public PostServiceImplementation postServiceImplementation;
    public CommentServiceImplementation commentServiceImplementation;
    public CommentController(PostServiceImplementation postServiceImplementation, CommentServiceImplementation commentServiceImplementation) {
        this.postServiceImplementation = postServiceImplementation;
        this.commentServiceImplementation = commentServiceImplementation;
    }
    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long postId,
            @RequestParam(name = "commentId", required = false) Long commentId,
            @RequestParam(name = "commentname") String commentName,
            Model model
    ) {
        Post post = postServiceImplementation.findPostById(postId);
        Long id = commentServiceImplementation.setCommentsForPost(post, commentId, commentName);
        postServiceImplementation.save(post);
        model.addAttribute("commentId", id);
        return "redirect:/post"+post.getId();
    }
    @PostMapping("/deletecomment/comment{comment_id}")
    public String deleteComment(
            @PathVariable("comment_id") Long id
    ) {
        Comment comment = commentServiceImplementation.findCommentById(id);
        Long postId = comment.getPost().getId();
        commentServiceImplementation.deleteCommentById(id);
        return "redirect:/post"+postId;
    }

    @PostMapping("/updatecomment/comment{comment_id}")
    public String updateComment(
            @PathVariable("comment_id") Long id,
            Model model
    ) {
        Comment comment = commentServiceImplementation.findCommentById(id);
        Post post = comment.getPost();
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("commentId",comment.getId());
        model.addAttribute("commentname", comment.getComment());
        return "showSinglePost";
    }
}
