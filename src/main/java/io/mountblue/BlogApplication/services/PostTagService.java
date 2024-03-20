package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;

import java.util.List;

public interface PostTagService {
    List<Post> findAllPostsByTags(List<Tag> tagIds);
}