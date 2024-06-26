package com.ti.youtubeminer.video.topic;


import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITopicRepository extends IBaseRepository<Topic> {
    Optional<Topic> findByLink(String link);
}
