package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import java.util.List;

public interface PostTagService {
    List<Post> findAllPostsByTags(List<Long> tagIds);
}