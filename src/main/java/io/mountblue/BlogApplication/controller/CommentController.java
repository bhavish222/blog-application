package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.services.CommentService;
import io.mountblue.BlogApplication.services.PostService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.mountblue.BlogApplication.entity.Comment;
import org.springframework.stereotype.Controller;
import io.mountblue.BlogApplication.entity.Post;
import org.springframework.ui.Model;

@Controller
public class CommentController {
     private PostService postService;
     private CommentService commentService;
    public CommentController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long postId,
            @RequestParam(name = "commentId", required = false) Long commentId,
            @RequestParam(name = "commentName") String commentName,
            @RequestParam(name = "commentEmail") String commentEmail,
            @RequestParam(name = "commentContent") String commentContent,
            Model model
    ) {
        Post post = postService.findPostById(postId);
        Long id = commentService.setCommentsForPost(post, commentId, commentName, commentEmail, commentContent);
        postService.save(post);
        model.addAttribute("commentId", id);
        return "redirect:/post"+post.getId();
    }

    @PostMapping("/deletecomment/comment{comment_id}")
    public String deleteComment(
            @PathVariable("comment_id") Long id
    ) {
        Comment comment = commentService.findCommentById(id);
        Long postId = comment.getPost().getId();
        commentService.deleteCommentById(id);
        return "redirect:/post"+postId;
    }

    @PostMapping("/updatecomment/comment{comment_id}")
    public String updateComment(
            @PathVariable("comment_id") Long id,
            Model model
    ) {
        Comment comment = commentService.findCommentById(id);
        Post post = comment.getPost();
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("commentId",comment.getId());
        model.addAttribute("commentname", comment.getComment());
        return "showSinglePost";
    }

}