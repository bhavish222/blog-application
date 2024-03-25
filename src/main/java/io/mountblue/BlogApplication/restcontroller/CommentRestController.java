package io.mountblue.BlogApplication.restcontroller;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.services.CommentService;
import io.mountblue.BlogApplication.services.PostService;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private PostService postService;
    private CommentService commentService;
    private UserService userService;
    public CommentRestController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }
    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long postId,
            @RequestBody Comment comment
    ) {
        Post post = postService.findPostById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findUserByName(loggedInUser);
        String email = user.getEmail();
        Comment newComment = new Comment();
        newComment.setComment(comment.getComment());
        newComment.setName(loggedInUser);
        newComment.setEmail(email);
        newComment.setPost(post);
        commentService.save(newComment);
        newComment.setPost(post);
        postService.save(post);
        return "success";
    }
    @DeleteMapping("/deletecomment/comment{comment_id}")
    public String deleteComment(
            @PathVariable("comment_id") Long id
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = commentService.findCommentById(id);
        String author = comment.getPost().getAuthor().getName();
        String loggedInUser = authentication.getName();
        if(authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !author.equals(loggedInUser)) {
            return "access-denied";
        }
        commentService.deleteCommentById(id);
        return "deleted comment";
    }

    @PutMapping("/updatecomment/comment{comment_id}")
    public String updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody Comment comment
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        Comment existingComment = commentService.findCommentById(commentId);
        String author = existingComment.getPost().getAuthor().getName();
        if(authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !author.equals(loggedInUser)) {
            return "access-denied";
        }
        existingComment.setComment(comment.getComment());
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        commentService.save(existingComment);
        return "success";
    }
}
