package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImplementation implements TagService{

    private TagRepository tagRepository;

    public TagServiceImplementation(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }
}
