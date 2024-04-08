package com.ti.youtubeminer.video.topic;

import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService extends BaseService<ITopicRepository, Topic> {

    @Autowired
    private ITopicRepository topicRepository;

    public List<Topic> saveAllList(List<Topic> topics) {
        return topics.stream().map(t -> create(t)).collect(Collectors.toList());
    }

    private Topic create(Topic t) {
        Optional<Topic> topic = topicRepository.findByLink(t.getLink());
        if( topic.isPresent()){
            return topic.get();
        }
        return this.save(t);
    }
}
