package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public void saveOrUpdate(Post post, String tagsString) {
        post.setIs_published(true);
        String[] tagNames = tagsString.split(",");
        List<Tag> newTags= new ArrayList<>();
        List<Tag> existingTags = tagRepository.findAll();
        List<Tag> allUniqueTagsForPost = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            newTags.add(tag);
        }

        for(Tag tag : newTags) {
            String tagName = tag.getName();
            boolean checkIfExists = false;
            for(Tag tempTag : existingTags) {
                String tempTagName = tempTag.getName();
                if(tempTagName.equals(tagName)) {
                    checkIfExists = true;
                    allUniqueTagsForPost.add(tempTag);
                    break;
                }
            }
            if(checkIfExists == false) {
                allUniqueTagsForPost.add(tag);
            }
        }

        post.setTags(allUniqueTagsForPost);

        int currentPostLength = post.getContent().length();
        String excerpt = post.getContent().substring(0, Math.min(currentPostLength, 150));
        if(post.getId() != null) {
            Post existingPost = findPostById(post.getId());
            if(existingPost != null) {
                existingPost.setTitle(post.getTitle());
                existingPost.setContent(post.getContent());
                existingPost.setTags(post.getTags());
                existingPost.setExcerpt(post.getExcerpt());
                existingPost.setAuthor(post.getAuthor());
                existingPost.setPublishedAt(post.getPublishedAt());
                existingPost.setExcerpt(post.getExcerpt());
            }
        } else {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        }
        post.setExcerpt(excerpt);
        post.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public List<Post> findPostsByAuthorIn(List<User> userList) {
        return postRepository.findPostsByAuthorIn(userList);
    }

    @Override
    public List<Post> findPostsByPublishedAtDateRange(String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDateStr, formatter);
        LocalDate end = LocalDate.parse(endDateStr, formatter);

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atStartOfDay();
        return postRepository.findPostsByPublishedAtDateRange(startDate, endDate);
    }

    public List<Post> findAllPostsByIdsIn(List<Long> postsIdList) {
        return postRepository.findAllPostsByIdIn(postsIdList);
    }
}