package com.ti.youtubeminer.miner.videominer;

import com.google.api.services.youtube.model.*;
import com.ti.youtubeminer.enums.ContentTypeEnum;
import com.ti.youtubeminer.enums.ProgrammingLanguageEnum;
import com.ti.youtubeminer.kind.KindService;
import com.ti.youtubeminer.pageinfo.PageInfo;
import com.ti.youtubeminer.pageinfo.PageInfoService;
import com.ti.youtubeminer.tag.Tag;
import com.ti.youtubeminer.tag.TagService;
import com.ti.youtubeminer.utils.VideoUtils;
import com.ti.youtubeminer.video.Video;
import com.ti.youtubeminer.video.VideoService;
import com.ti.youtubeminer.video.language.LanguageService;
import com.ti.youtubeminer.video.localized.Localized;
import com.ti.youtubeminer.video.localized.LocalizedService;
import com.ti.youtubeminer.video.statistics.Statistics;
import com.ti.youtubeminer.video.statistics.StatisticsService;
import com.ti.youtubeminer.video.status.Status;
import com.ti.youtubeminer.video.status.StatusService;
import com.ti.youtubeminer.video.topic.Topic;
import com.ti.youtubeminer.video.topic.TopicService;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponse;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoDataProcessor {

    @Autowired
    private YouTubeSearchListPageResponseService youTubeSearchListPageResponseService;

    @Autowired
    private PageInfoService pageInfoService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private KindService kindService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LocalizedService localizedService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    @Value("${application.mining.programming_language_mining_flag}")
    private String PROGRAMMING_LANGUAGE_MINING_FLAG;

    private ContentTypeEnum CONTENT_TYPE = ContentTypeEnum.VIDEO;

    @Transactional
    public void proccessVideoResponse(VideoListResponse response, YouTubeSearchListPageResponse youtubeSearchListResponse) {
        PageInfo pageInfo = PageInfo.builder()
                .resultsPerPage(response.getPageInfo().getResultsPerPage())
                .totalResults(response.getPageInfo().getTotalResults())
                .build();
        PageInfo savedPageInfo = pageInfoService.save(pageInfo);

        List<Video> videos = response.getItems().stream()
                .map(responseVideo -> buildVideo(responseVideo, youtubeSearchListResponse))
                .collect(Collectors.toList());

        youtubeSearchListResponse.setVideos(videos);
        youtubeSearchListResponse.setCompletelyProcessed(true);
        videoService.saveAll(videos);
        youTubeSearchListPageResponseService.save(youtubeSearchListResponse);
    }

    private PageInfo savePageInfo(PageInfo pageInfo) {
        return pageInfoService.save(PageInfo.builder()
                .resultsPerPage(pageInfo.getResultsPerPage())
                .totalResults(pageInfo.getTotalResults())
                .build());
    }

    private YouTubeSearchListPageResponse buildYouTubeVideoListResponse(VideoListResponse response, PageInfo savedPageInfo) {
        return YouTubeSearchListPageResponse.builder()
                .etag(response.getEtag())
                .eventId(response.getEventId())
                .kind(kindService.findByDescription(response.getKind()))
                .nextPageToken(response.getNextPageToken())
                .prevPageToken(response.getPrevPageToken())
                .visitorId(response.getVisitorId())
                .programmingLanguage(ProgrammingLanguageEnum.valueOf(PROGRAMMING_LANGUAGE_MINING_FLAG))
                .contentType(CONTENT_TYPE)
                .pageInfo(savedPageInfo)
                .completelyProcessed(true)
                .build();
    }

    public YouTubeSearchListPageResponse preProcessSearchListResponse(SearchListResponse response) {

        PageInfo pageInfo = PageInfo.builder()
                .resultsPerPage(response.getPageInfo().getResultsPerPage())
                .totalResults(response.getPageInfo().getTotalResults())
                .build();
        PageInfo savedPageInfo = pageInfoService.save(pageInfo);

        YouTubeSearchListPageResponse youTubeSearchListPageResponse = YouTubeSearchListPageResponse.builder()
                .completelyProcessed(false)
                .etag(response.getEtag())
                .contentType(ContentTypeEnum.VIDEO)
                .kind(kindService.findByDescription(response.getKind()))
                .eventId(response.getEventId())
                .nextPageToken(response.getNextPageToken())
                .prevPageToken(response.getPrevPageToken())
                .pageInfo(savedPageInfo)
                .programmingLanguage(ProgrammingLanguageEnum.valueOf(PROGRAMMING_LANGUAGE_MINING_FLAG))
                .visitorId(response.getVisitorId())
                .regionCode(response.getRegionCode())
                .build();

        return youTubeSearchListPageResponse;
    }


    private Video buildVideo(com.google.api.services.youtube.model.Video responseVideo, YouTubeSearchListPageResponse youTubeSearchListPageResponse) {
        VideoSnippet snippet = responseVideo.getSnippet();
        Date publishedAt = Date.from(Instant.parse(snippet.getPublishedAt().toStringRfc3339()));
        List<Tag> tags = processTags(snippet.getTags());
        List<Topic> topics = processTopics(responseVideo.getTopicDetails());
        Statistics statistics = buildStatistics(responseVideo.getStatistics());
        statistics.setVideoDuration(VideoUtils.convertToSeconds(responseVideo.getContentDetails().getDuration()));
        statistics.setVideoDurationOriginalString(responseVideo.getContentDetails().getDuration());

        return Video.builder()
                .etag(responseVideo.getEtag())
                .kind(kindService.findByDescription(responseVideo.getKind()))
                .videoId(responseVideo.getId())
                .channelId(snippet.getChannelId())
                .channelTitle(snippet.getChannelTitle())
                .description(snippet.getDescription())
                .title(snippet.getTitle())
                .publishedAt(publishedAt)
                .categoryId(snippet.getCategoryId())
                .liveBroadcastContent(snippet.getLiveBroadcastContent())
                .topics(topics)
                .defaultAudioLanguage(languageService.findByCode(snippet.getDefaultAudioLanguage()))
                .minedDetails(true)
                .localized(localizedService.save(buildLocalized(snippet)))
                .tags(tags)
                .status(statusService.save(buildStatus(responseVideo.getStatus())))
                .statistics(statisticsService.save(statistics))
                .build();
    }

    private Localized buildLocalized(VideoSnippet snippet) {
        return Localized.builder()
                .description(snippet.getLocalized().getDescription())
                .title(snippet.getLocalized().getTitle())
                .build();
    }

    private Status buildStatus(VideoStatus videoStatus) {
        return Status.builder()
                .embeddable(videoStatus.getEmbeddable())
                .license(videoStatus.getLicense())
                .privacyStatus(videoStatus.getPrivacyStatus())
                .madeForKids(videoStatus.getMadeForKids())
                .uploadStatus(videoStatus.getUploadStatus())
                .publicStatsViewable(videoStatus.getPublicStatsViewable())
                .build();
    }

    private Statistics buildStatistics(VideoStatistics videoStatistics) {
        return Statistics.builder()
                .commentCount(videoStatistics.getCommentCount())
                .favoriteCount(videoStatistics.getFavoriteCount())
                .likeCount(videoStatistics.getLikeCount())
                .viewCount(videoStatistics.getViewCount())
                .build();
    }

    private List<Topic> processTopics(VideoTopicDetails topicDetails) {
        if (topicDetails != null && topicDetails.getTopicCategories() != null) {
            return topicService.saveAllList(topicDetails.getTopicCategories().stream()
                    .map(t -> Topic.builder().link(t).build())
                    .collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<Tag> processTags(List<String> tagsString) {
        if (tagsString == null) {
            return new ArrayList<>();
        }
        List<Tag> tags = tagsString.stream().map(t -> Tag.builder().label(t).build()).collect(Collectors.toList());
        return tagService.saveAllList(tags);
    }

    public void processInvalidVideo(Video video) {
        List<Video> videos = videoService.findAllByVideoId(video.getVideoId());
        videos.forEach(v -> {
            v.setValidByMiner(false);
            v.setRemovedFromYouTube(true);
        });
        videoService.saveAll(videos);
    }
}
