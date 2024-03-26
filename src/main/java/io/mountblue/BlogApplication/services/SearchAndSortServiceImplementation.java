package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.PostRepository;
import io.mountblue.BlogApplication.repository.PostTagRepository;
import io.mountblue.BlogApplication.repository.TagRepository;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Service
public class SearchAndSortServiceImplementation implements SearchAndSortService {
    public SearchAndSortServiceImplementation() {}

    private PostRepository postRepository;
    private PostTagRepository postTagRepository;
    private UserRepository userRepository;

    @Autowired
    public SearchAndSortServiceImplementation(
            PostRepository postRepository,
            PostTagRepository postTagRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> toFindAllPostsForSearch(List<Post> allPostsList, String searchBarInput) {
        List<Post> filteredPostBasedOnSearch = new ArrayList<>();
        for(Post post : allPostsList) {
            if(post.getTitle().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            if(post.getContent().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            if(post.getAuthor().getName().toLowerCase().contains(searchBarInput.toLowerCase())) {
                filteredPostBasedOnSearch.add(post);
            }
            for(Tag tag : post.getTags()) {
                if(tag.getName().toLowerCase().contains(searchBarInput.toLowerCase())) {
                    filteredPostBasedOnSearch.add(post);
                    break;
                }
            }
        }
        return filteredPostBasedOnSearch;
    }

    @Override
    public List<Post> combineFilters(List<Post> postsForTags, List<Post> postsForUser, List<Post> postsForDate) {
        Set<Post> uniquePosts = new HashSet<>();
        uniquePosts.addAll(postsForTags);
        uniquePosts.addAll(postsForUser);
        uniquePosts.addAll(postsForDate);
        System.out.println(uniquePosts);
        return new ArrayList<Post>(uniquePosts);
    }

    @Override
    public List<Post> searchPostBySearchBarInput(String searchBarInput) {
        List<Post> searchedPosts = postRepository.findAll();
        if(searchBarInput != null && !searchBarInput.isEmpty()) {
            searchedPosts = toFindAllPostsForSearch(searchedPosts, searchBarInput);
        }
        else {
            searchedPosts = postRepository.findAll();
        }
        return searchedPosts;
    }

    @Override
    public List<Post> filteredByPosts(String startDateStr, String endDateStr, List<Long> tagId, List<Long> userId) {
        if(startDateStr == null) {
            startDateStr = "";
            endDateStr = "";
        }
        List<Post> postsForTags = new ArrayList<>();
        List<Post> postsForUser = new ArrayList<>();
        List<Post> postsForDate = new ArrayList<>();

        if(tagId != null) {
            postsForTags = postTagRepository.findPostIdsByTagIds(tagId);
        }
        if(userId != null) {
            List<User> authorList = userRepository.findAuthorByIdIn(userId);
            postsForUser = postRepository.findPostsByAuthorIn(authorList);
        }
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDateStr, formatter);
            LocalDate end = LocalDate.parse(endDateStr, formatter);

            LocalDateTime startDate = start.atStartOfDay();
            LocalDateTime endDate = end.atStartOfDay();
            postsForDate = postRepository.findPostsByPublishedAtDateRange(startDate, endDate);
        }
        return combineFilters(postsForTags, postsForUser, postsForDate);
    }

    @Override
    public List<Post> checkForSearchedAndFiltered(List<Post> searchedPosts, List<Post> filteredPosts) {
        List<Post> commonPosts = new ArrayList<>();
        if(!filteredPosts.isEmpty()) {
            for(Post post : searchedPosts) {
                if(filteredPosts.contains(post)) {
                    commonPosts.add(post);
                }
            }
        }
        else {
            commonPosts = searchedPosts;
        }
        return commonPosts;
    }

    @Override
    public Page<Post> sort(List<Post> commonPosts, String sort, Pageable pageable) {
        Page<Post> sortedPosts;
        if(sort.equals("newest")) {
            sortedPosts = postRepository.findPostsInAndOrderByPublishedAtDesc(commonPosts, pageable);
        }
        else {
            sortedPosts = postRepository.findPostsInAndOrderByPublishedAtAsc(commonPosts, pageable);
        }
        return sortedPosts;
    }
}