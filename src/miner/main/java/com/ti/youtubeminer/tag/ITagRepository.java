package com.ti.youtubeminer.tag;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITagRepository extends IBaseRepository<Tag> {
    Optional<Tag> findByLabel(String label);
}
