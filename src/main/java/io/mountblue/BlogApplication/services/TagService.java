package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> findAllTags();
}
