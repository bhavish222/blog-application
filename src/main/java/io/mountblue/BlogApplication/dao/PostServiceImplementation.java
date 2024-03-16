package io.mountblue.BlogApplication.dao;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImplementation implements PostService{

    public PostServiceImplementation() {}

    private UserRepository userRepository;
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private PostTagRepository postTagRepository;
    private CommentRepository commentRepository;

    @Autowired
    public PostServiceImplementation(UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository, PostTagRepository postTagRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }
    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }
    @Override
    public Post findPostById(Long id){
        return postRepository.findPostById(id);
    }
    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
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
    public List<Post> getAllPostsSortedByDate() {
        return postRepository.findAllByOrderByPublishedAtDesc();
    }
    @Override
    public List<Post> getAllPostsSortedByOldestDate() {
        return postRepository.findAllByOrderByPublishedAtAsc();
    }
}
