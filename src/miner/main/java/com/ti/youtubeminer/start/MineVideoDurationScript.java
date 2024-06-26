package com.ti.youtubeminer.start;

import com.google.api.services.youtube.model.VideoListResponse;
import com.ti.youtubeminer.global.domain.annotations.Unused;
import com.ti.youtubeminer.miner.videominer.VideoDataProcessor;
import com.ti.youtubeminer.miner.videominer.VideoMiner;
import com.ti.youtubeminer.missioncotrol.EnvironmentChecker;
import com.ti.youtubeminer.utils.VideoUtils;
import com.ti.youtubeminer.video.Video;
import com.ti.youtubeminer.video.VideoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Unused
public class MineVideoDurationScript {

    @Autowired
    private VideoMiner videoMiner;

    @Autowired
    private VideoDataProcessor videoDataProcessor;

    @Autowired
    private VideoService videoService;

    @Autowired
    private EnvironmentChecker environmentChecker;

    public void letsGo() throws Exception {

        if (environmentChecker.isTestEnvironment()) {
            return;
        }

        List<Video> uniqueVideos = videoService.findAllUniqueVideos();

        while (!uniqueVideos.isEmpty()) {
            List<Video> list50 = getFirst50(uniqueVideos);
            uniqueVideos = removeFirst50(uniqueVideos);
            List<String> videoIds = list50.stream().map(Video::getVideoId).collect(Collectors.toList());

            VideoListResponse videoListResponse = videoMiner.mineVideoDetailsByIdList(String.join(",", videoIds));

            list50.forEach(video -> {
                Optional<com.google.api.services.youtube.model.Video> respective = findRespectiveByIdInList(videoListResponse.getItems(), video);
                respective.ifPresentOrElse(ytVideo -> extractVideoDuration(ytVideo, video), () -> {
                    videoDataProcessor.processInvalidVideo(video);
                });
            });
        }
        System.out.println("\n\n\n\n\n===========================\n\n SESSÃO DE MINERAÇÃO COMPLEMENTAR: COMPLETA \n\n===========================\n\n\n\n\n");
    }

    private Optional<com.google.api.services.youtube.model.Video> findRespectiveByIdInList(
            List<com.google.api.services.youtube.model.Video> items, Video video
    ) {
        return items.stream()
                .filter(item -> item.getId().equals(video.getVideoId()))
                .findFirst();
    }

    private List<Video> removeFirst50(List<Video> videos) {
        if (videos.size() >= 50) {
            return videos.subList(50, videos.size());
        }
        return new ArrayList<Video>();
    }

    private List<Video> getFirst50(List<Video> videos) {
        return videos.size() >= 50 ? videos.subList(0, 50) : videos;
    }

    @Transactional
    public void extractVideoDuration(com.google.api.services.youtube.model.Video ytVideo, Video ourVideo) {
        BigInteger duration = VideoUtils.convertToSeconds(ytVideo.getContentDetails().getDuration());
        String durationOriginalString = ytVideo.getContentDetails().getDuration();

        List<Video> duplicates = videoService.findAllByVideoId(ourVideo.getVideoId());

        duplicates.forEach(duplicate -> {
            duplicate.getStatistics().setVideoDuration(duration);
            duplicate.getStatistics().setVideoDurationOriginalString(durationOriginalString);
            duplicate.setMinedDetails(true);
        });

        videoService.saveAll(duplicates);
    }
}

