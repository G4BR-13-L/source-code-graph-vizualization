package com.ti.youtubeminer.video;

import com.ti.youtubeminer.global.domain.exceptions.EntityNotFoundException;
import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService extends BaseService<IVideoRepository, Video> {

    @Autowired
    private IVideoRepository videoRepository;

    public Video findByYouTubeId(String videoId) {
        return videoRepository.findFirstByVideoId(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video do Id " + videoId + " não encontrado", HttpStatus.NOT_FOUND));
    }

    public List<Video> findAllByVideoId(String id){
        return videoRepository.findAllByVideoId(id);
    }

    public List<Video> saveAllUniqueVideos(List<Video> videos) {
        List<Video> existingVideos = videoRepository.findByVideoIdIn(
                videos.stream()
                        .map(Video::getVideoId)
                        .collect(Collectors.toList())
        );

        List<Video> uniqueVideos = videos.stream()
                .filter(video -> existingVideos.stream().noneMatch(existingVideo -> existingVideo.getVideoId().equals(video.getVideoId())))
                .collect(Collectors.toList());

        return videoRepository.saveAll(uniqueVideos);
    }

    public Long countUniqueVideos() {
        return videoRepository.countUniqueVideos();
    }

    public Long countMinedDetatilsVideos() {
        return videoRepository.countMinedDetatilsVideos();
    }

    public List<Video> findAllUniqueVideos(){
        return videoRepository.findAllUniqueVideos();
    }

    public List<Video> findAllByVideoIdIn(List<String> videoIds) {
        return videoRepository.findAllByVideoIdIn(videoIds);
    }

    public Long countInvalidVideos() {
        return videoRepository.countInvalidVideos();
    }

    public Long countRemovedVideos() {
        return videoRepository.countRemovedVideos();
    }
}
