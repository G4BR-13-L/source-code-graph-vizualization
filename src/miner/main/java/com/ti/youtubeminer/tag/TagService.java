package com.ti.youtubeminer.tag;


import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService extends BaseService<ITagRepository, Tag> {

    @Autowired
    private ITagRepository tagRepository;

    public List<Tag> saveAllList(List<Tag> tags) {
        return tags.stream().map(t -> create(t)).collect(Collectors.toList());
    }

    private Tag create(Tag t) {
        Optional<Tag> tag = tagRepository.findByLabel(t.getLabel());
        return tag.orElseGet(() ->
                this.save(t));
    }
}
