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

    public CommentRestController(
            PostService postService,
            CommentService commentService,
            UserService userService
    ) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/postcomment/post{post_id}")
    public String postComment(
            @PathVariable("post_id") Long postId,
            @RequestBody Comment comment
    ) {
        commentService.setCommentsForPostForRest(postId, comment);
        return "success";
    }

    @DeleteMapping("/deletecomment/comment{comment_id}")
    public String deleteComment(
            @PathVariable("comment_id") Long id
    ) {
        return commentService.deleteCommentByIdForRest(id);
    }

    @PutMapping("/updatecomment/comment{comment_id}")
    public String updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody Comment comment
    ) {
        return commentService.updateCommentForRest(commentId, comment);
    }
}