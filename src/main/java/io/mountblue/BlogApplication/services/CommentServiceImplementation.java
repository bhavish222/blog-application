package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService{
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public CommentServiceImplementation() {

    }
    @Autowired
    public CommentServiceImplementation(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findCommentById(id);
    }
    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }
    @Override
    public Long setCommentsForPost(
            Post post,
            Long commentId,
            String commentName,
            String commentEmail,
            String commentContent
    ) {
        List<Comment> listOfComment = post.getComments();
        Comment comment;
        if(commentId == null) {
            comment = new Comment();
        } else {
            comment = findCommentById(commentId);
        }
        comment.setName(commentName);
        comment.setEmail(commentEmail);
        comment.setComment(commentContent);
        listOfComment.add(comment);
        post.setComments(listOfComment);
        comment.setPost(post);
        return commentId;
    }

    @Override
    public void save(Comment newComment) {
        commentRepository.save(newComment);
    }

    @Override
    public void setCommentsForPostForRest(Long postId, Comment comment) {
        Post post = postRepository.findPostById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userRepository.findUserByName(loggedInUser);
        String email = user.getEmail();
        Comment newComment = new Comment();
        newComment.setComment(comment.getComment());
        newComment.setName(loggedInUser);
        newComment.setEmail(email);
        newComment.setPost(post);
        commentRepository.save(newComment);
        newComment.setPost(post);
        postRepository.save(post);
    }

    @Override
    public String deleteCommentByIdForRest(Long id) {
        Comment comment = findCommentById(id);
        String postAuthor = comment.getPost().getAuthor().getName();
        String loggedInUser = findLoggedInUser();
        String authoritiesForUser = findAuthoritiesForUser();
        if (authoritiesForUser.equals("[ROLE_AUTHOR]") && !postAuthor.equals(loggedInUser)) {
            return "access-denied";
        }
        deleteCommentById(id);
        return "deleted comment successfully";
    }

    @Override
    public String updateCommentForRest(Long commentId, Comment comment) {
        String loggedInUser = findLoggedInUser();
        String authoritiesForUser = findAuthoritiesForUser();
        Comment existingComment = findCommentById(commentId);
        String postAuthor = existingComment.getPost().getAuthor().getName();
        if (authoritiesForUser.equals("[ROLE_AUTHOR]") && !postAuthor.equals(loggedInUser)) {
            return "access-denied";
        }
        existingComment.setComment(comment.getComment());
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        save(existingComment);
        return "successfully updated comment";
    }

    private String findAuthoritiesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().toString();
    }

    private String findLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}