package com.ti.youtubeminer.start;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.config.BootstrapOnReady;
import com.ti.youtubeminer.enums.ContentTypeEnum;
import com.ti.youtubeminer.global.domain.exceptions.ApiKeyException;
import com.ti.youtubeminer.miner.videominer.VideoDataProcessor;
import com.ti.youtubeminer.miner.videominer.VideoMiner;
import com.ti.youtubeminer.missioncotrol.EnvironmentChecker;
import com.ti.youtubeminer.missioncotrol.MissionControlService;
import com.ti.youtubeminer.report.ReportService;
import com.ti.youtubeminer.searchterm.SearchTerm;
import com.ti.youtubeminer.searchterm.SearchTermService;
import com.ti.youtubeminer.video.VideoService;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponse;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class Start {

    @Value("${application.mining.programming_language_mining_flag}")
    private String PROGRAMMING_LANGUAGE_MINING_FLAG;

    @Value("${application.mining.search_terms}")
    private String SEARCH_TERMS;

    @Value("${application.mining.video_amount}")
    private String VIDEO_AMOUNT;

    @Autowired
    private VideoMiner videoMiner;

    @Autowired
    private VideoDataProcessor videoDataProcessor;

    @Autowired
    private VideoService videoService;

    @Autowired
    private YouTubeSearchListPageResponseService youTubeSearchListPageResponseService;

    @Autowired
    private MissionControlService missionControlService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private SearchTermService searchTermService;

    @Autowired
    private EnvironmentChecker environmentChecker;

    @Autowired
    private BootstrapOnReady bootstrapOnReady;

    @Autowired
    private ReportService reportService;

    @Autowired
    private MineVideoDurationScript mineVideoDurationScript;

    @EventListener(value = ApplicationReadyEvent.class)
    @Profile(value = "dev")
    public void letsGo() throws Exception {

        bootstrapOnReady.bootstraper();
        missionControlService.init();
        missionControlService.printUsedApiKey();

        if (environmentChecker.isTestEnvironment()) {
            return;
        }

        if (youTubeSearchListPageResponseService.weDoNotHavePages() && !this.verifyMiningDataAmount()) {
            mineTheFirstVideoPage();
        }

        // ======================================================
        // MINERAÇÃO DA DURAÇÃO DOS VIDEOS DESABILITADA
//        boolean mineVideoDuration = true;
//        if(mineVideoDuration){
//            mineVideoDurationScript.letsGo();
//            return;
//        }
        // ======================================================

        int videoRequestAmount = (Integer.parseInt(VIDEO_AMOUNT) / 50);
        List<SearchTerm> searchTermsTotal = searchTermService.findAll();
        List<SearchTerm> searchTerms = searchTermService.findAllUnsearched();
        int videoRequestAmountPerTerm = Math.max(videoRequestAmount / searchTermsTotal.size(), 8);

        for (int i = 0; i < searchTerms.size(); i++) {
            SearchTerm searchTerm = searchTerms.get(i);
            for (int j = 0; j < videoRequestAmountPerTerm; j++) {
                if (j % 4 == 0) {
                    if (this.verifyMiningDataAmount()) {
                        System.out.println("\n\n\n\n===================================================\n\nMineração de videos completa!\n\n===================================================\n\n\n\n");
                        return;
                    }
                }
                YouTubeSearchListPageResponse previousListResponse = youTubeSearchListPageResponseService.findLatestByContentType(ContentTypeEnum.VIDEO);
                SearchListResponse searchListResponse = videoMiner.mineVideos(previousListResponse.getNextPageToken(), searchTerm);
                YouTubeSearchListPageResponse buildedSearchListResponse = videoDataProcessor.preProcessSearchListResponse(searchListResponse);
                VideoListResponse videoListResponse = videoMiner.mineVideoDetails(searchListResponse);
                videoDataProcessor.proccessVideoResponse(videoListResponse, buildedSearchListResponse);
            }
            searchTerm.setSearched(true);
            searchTermService.save(searchTerm);
        }

        reportService.writeEndReportToFile();
    }

    public boolean verifyMiningDataAmount() {
        Long videoSpecifiedAmount = Long.parseLong(VIDEO_AMOUNT);
        Long minVideos = (long) (videoSpecifiedAmount - (videoSpecifiedAmount * 0.10));
        Long maxVideos = (long) (videoSpecifiedAmount + (videoSpecifiedAmount * 0.10));

        if (videoService.countUniqueVideos() > minVideos && videoService.countUniqueVideos() < maxVideos) {
            return true;
        }
        return false;
    }

    public void mineTheFirstVideoPage() throws GeneralSecurityException, IOException, ApiKeyException, InterruptedException {
        SearchListResponse firstSearchListResponse = videoMiner.mineVideos();
        YouTubeSearchListPageResponse buildedFirstSearchListResponse = videoDataProcessor.preProcessSearchListResponse(firstSearchListResponse);
        VideoListResponse firstVideoListResponse = videoMiner.mineVideoDetails(firstSearchListResponse);
        videoDataProcessor.proccessVideoResponse(firstVideoListResponse, buildedFirstSearchListResponse);
    }
}
