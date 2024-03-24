package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public PostServiceImplementation(
            PostRepository postRepository,
            TagRepository tagRepository
    ) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
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
        User user = new User(1L, "bhavi", "wadhwabhavish46@gmail.com", "3001");
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
                existingPost.setAuthor(user);
                save(existingPost);
            }
        }
        post.setAuthor(user);
        save(post);
    }

    @Override
    public List<Post> findPostsByAuthorIn(List<Long> userList) {
        return postRepository.findPostsByAuthorIdIn(userList);
    }

    @Override
    public List<Post> findPostsByPublishedAtDateRange(String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(startDateStr + "\n\n\n" + endDateStr + "\n\n\n");
        LocalDate start = LocalDate.parse(startDateStr, formatter);
        LocalDate end = LocalDate.parse(endDateStr, formatter);

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atStartOfDay();
        return postRepository.findPostsByPublishedAtDateRange(startDate, endDate);
    }
}