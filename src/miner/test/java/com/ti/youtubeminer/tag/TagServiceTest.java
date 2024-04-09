package com.ti.youtubeminer.tag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    void testSaveAllList() {
        List<Tag> tags = Arrays.asList(
                Tag.builder().label("Tag1").build(),
                Tag.builder().label("Tag2").build(),
                Tag.builder().label("Tag3").build()
                );
        List<Tag> savedTags = tagService.saveAllList(tags);
        assertEquals(3, savedTags.size());
    }

    @Test
    void testCreate() {
        Tag tagToCreate = Tag.builder().label("NewTag").build();
        Tag createdTag = tagService.save(tagToCreate);
        assertEquals("NewTag", createdTag.getLabel());
    }
}