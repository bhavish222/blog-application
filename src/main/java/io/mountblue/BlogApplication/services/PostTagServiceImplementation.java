package io.mountblue.BlogApplication.services;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.repository.PostTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagServiceImplementation implements PostTagService{
    private PostTagRepository postTagRepository;

    public PostTagServiceImplementation(PostTagRepository postTagRepository) {
        this.postTagRepository = postTagRepository;
    }

    @Override
    public List<Post> findAllPostsByTags(List<Long> tagIds) {
        return postTagRepository.findPostIdsByTagIds(tagIds);
    }
}