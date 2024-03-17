package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
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

    @Autowired
    public PostServiceImplementation(UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository, PostTagRepository postTagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
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
    public List<Post> getPostsSortedByDate(List<Post> posts) {
        return postRepository.findPostsInAndOrderByPublishedAtDesc(posts);
    }
    @Override
    public List<Post> getPostsSortedByOldestDate(List<Post> posts) {
        return postRepository.findPostsInAndOrderByPublishedAtAsc(posts);
    }
    @Override
    public String findTagsOfPostToString(Post post) {
        List<Tag> listOfTags = post.getTags();
        StringBuilder tagListBuilder = new StringBuilder();
        if (listOfTags != null) {
            for(Tag tag : listOfTags) {
                tagListBuilder.append(tag.getName()).append(",");
            }
        }
        return tagListBuilder.toString();
    }
}