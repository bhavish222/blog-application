package io.mountblue.BlogApplication.dao;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImplementation{

    public ServiceImplementation() {}

    private UserRepository userRepository;
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private PostTagRepository postTagRepository;
    private CommentRepository commentRepository;

    @Autowired
    public ServiceImplementation(UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository, PostTagRepository postTagRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
        this.commentRepository = commentRepository;
    }

    public void save(Post post) {
        postRepository.save(post);
    }
    public List<Post> showAllPosts() {
        List<Post> allPosts=postRepository.findAll();
        return allPosts;
    }
    public Post findPostById(Long id){
        return postRepository.findPostById(id);
    }
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}
