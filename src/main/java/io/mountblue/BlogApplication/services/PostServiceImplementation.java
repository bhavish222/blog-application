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
    public Page<Post> paginationPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return postRepository.findAll(pageable);
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
            for (Tag tag : listOfTags) {
                tagListBuilder.append(tag.getName()).append(",");
            }
        }
        return tagListBuilder.toString();
    }

    @Override
    public void saveOrUpdate(Post post, String tagsString, String action) {
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

        if (action.equals("Publish")) {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
            post.setIs_published(true);
        }
        User user = new User(1L, "bhavi", "wadhwabhavish46@gmail.com", "3001");
        post.setAuthor(user);
        post.setExcerpt(excerpt);
        post.setUpdatedAt(LocalDateTime.now());
        if (post.getId() != null) {
            Post existingPost = findPostById(post.getId());
            if (existingPost != null) {
                existingPost.setTitle(post.getTitle());
                existingPost.setContent(post.getContent());
                existingPost.setTags(post.getTags());
                existingPost.setExcerpt(post.getExcerpt());
                existingPost.setAuthor(post.getAuthor());
                existingPost.setPublishedAt(post.getPublishedAt());
                existingPost.setExcerpt(post.getExcerpt());
            }
        }
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

    public List<Post> findAllPostsByIdIn(List<Long> postsIdList) {
        return postRepository.findAllPostsByIdIn(postsIdList);
    }
}