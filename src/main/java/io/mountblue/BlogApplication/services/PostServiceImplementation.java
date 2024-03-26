package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImplementation implements PostService {

    public PostServiceImplementation() {

    }
    private PostRepository postRepository;
    private TagRepository tagRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImplementation(
            PostRepository postRepository,
            TagRepository tagRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
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
    public Post findPostById(Long id) {
        return postRepository.findPostById(id);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public String findTagsOfPostToString(Post post) {
        List<Tag> listOfTags = post.getTags();
        StringBuilder tagListBuilder = new StringBuilder();
        if (listOfTags != null) {
            for (Tag tag : listOfTags) {
                tagListBuilder.append(tag.getName()).append(",");
            }
        }
        return tagListBuilder.toString();
    }

    @Override
    public void saveOrUpdate(Post post, String tagsString, String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        User currentLoggedInUser = userRepository.findUserByName(user);
        if(action.equals("Publish")) {
            post.setIs_published(true);
            post.setPublishedAt(LocalDateTime.now());
        }
        String[] tagNames = tagsString.split(",");
        List<Tag> newTags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            newTags.add(tag);
        }
        List<Tag> existingTags = tagRepository.findAll();
        List<Tag> allUniqueTagsForThisPost = new ArrayList<>();
        for (Tag tag : newTags) {
            String tagName = tag.getName();
            boolean checkIfExists = false;
            for (Tag tempTag : existingTags) {
                String tempTagName = tempTag.getName();
                if (tempTagName.equals(tagName)) {
                    checkIfExists = true;
                    allUniqueTagsForThisPost.add(tempTag);
                    break;
                }
            }
            if (!checkIfExists) {
                allUniqueTagsForThisPost.add(tag);
            }
        }
        post.setTags(allUniqueTagsForThisPost);
        int currentPostLength = post.getContent().length();
        String excerpt = post.getContent().substring(0, Math.min(currentPostLength, 150));
        post.setExcerpt(excerpt);
        if (post.getId() != null) {
            Post existingPost = findPostById(post.getId());
            post.setIs_published(true);
            if (existingPost != null) {
                existingPost.setTitle(post.getTitle());
                existingPost.setContent(post.getContent());
                existingPost.setTags(post.getTags());
                existingPost.setExcerpt(post.getExcerpt());
                existingPost.setAuthor(post.getAuthor());
                existingPost.setUpdatedAt(LocalDateTime.now());
                existingPost.setTags(post.getTags());
                existingPost.setAuthor(currentLoggedInUser);
                save(existingPost);
            }
        }
        post.setAuthor(currentLoggedInUser);
        save(post);
    }

    @Override
    public String deletePostByIdForRest(Long id) {
        String loggedInUser = findLoggedInUser();
        String authoritiesForUser = findAuthoritiesForUser();
        User user = userRepository.findUserByName(loggedInUser);
        Post post = postRepository.findPostById(id);
        if(authoritiesForUser.equals("[ROLE_AUTHOR]") && !post.getAuthor().getName().equals(user.getName())) {
            return "access-denied";
        }
        deletePostById(id);
        return "successfully deleted post";
    }

    @Override
    public String updatePostByIdForRest(Long id, Post updatedPost) {
        String loggedInUser = findLoggedInUser();
        String authoritiesForUser = findAuthoritiesForUser();
        User user = userRepository.findUserByName(loggedInUser);
        Post post = postRepository.findPostById(id);
        if (authoritiesForUser.equals("[ROLE_AUTHOR]") && !post.getAuthor().getName().equals(loggedInUser)) {
            return "access-denied";
        }
        post = updatedPost;
        post.setAuthor(user);
        save(post);
        return "successfully updated";
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