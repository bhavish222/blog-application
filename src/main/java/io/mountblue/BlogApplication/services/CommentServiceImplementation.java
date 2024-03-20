package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService{
    public CommentServiceImplementation() {

    }
    private CommentRepository commentRepository;
    @Autowired
    public CommentServiceImplementation(
            CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
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
            String commentName
    ) {
        List<Comment> listOfComment = post.getComments();
        Comment comment;
        if(commentId == null) {
            comment = new Comment();
        } else {
            comment = findCommentById(commentId);
        }
        comment.setComment(commentName);
        listOfComment.add(comment);
        post.setComments(listOfComment);
        comment.setPost(post);
        return commentId;
    }
}