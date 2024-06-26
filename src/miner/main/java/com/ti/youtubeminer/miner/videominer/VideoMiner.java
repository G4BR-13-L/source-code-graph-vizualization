package com.ti.youtubeminer.miner.videominer;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.global.domain.exceptions.ApiKeyException;
import com.ti.youtubeminer.missioncotrol.MissionControlService;
import com.ti.youtubeminer.searchterm.SearchTerm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class VideoMiner {
    @Value("${application.mining.search_terms}")
    private String searchTerms;

    @Autowired
    private MissionControlService missionControlService;

    @Autowired
    private ApiKeyService apiKeyService;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "YouTube Miner";
    private static final String VIDEO_TYPE = "video";
    private static final String VIDEO_DETAILS_FIELDS = "snippet,statistics,status,topicDetails, contentDetails";
    private static final long MAX_RESULTS = 50L;

    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private SearchListResponse executeSearchRequest(String nextPageToken, SearchTerm searchTerm)
            throws GeneralSecurityException, IOException, ApiKeyException, InterruptedException {
        YouTube youtubeService = getService();
        SearchListResponse response = null;
        int maxAttempts = 7; // Número máximo de tentativas

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                YouTube.Search.List request = youtubeService.search().list("snippet");
                request.setKey(apiKeyService.getMinigKeyString().get())
                        .setPageToken(nextPageToken)
                        .setMaxResults(MAX_RESULTS)
                        .setQ(searchTerm.getTerm())
                        .setType(VIDEO_TYPE);
                response = request.execute();
            } catch (IOException e) {
                handleApiException(e);
            }
            Thread.sleep(200);
            if(response != null){
                attempt += 1000;
            }
        }
        return response;
    }

    public SearchListResponse mineVideos(String nextPageToken, SearchTerm searchTerm)
            throws GeneralSecurityException, IOException, ApiKeyException, InterruptedException {
        SearchListResponse response;
        response = executeSearchRequest(nextPageToken, searchTerm);
        logResponse(response);
        return response;
    }

    public SearchListResponse mineVideos()
            throws GeneralSecurityException, IOException, ApiKeyException, InterruptedException {
        SearchListResponse response;
        response = executeSearchRequest(null, SearchTerm.builder().term(searchTerms).build());
        logResponse(response);
        return response;
    }

    public VideoListResponse mineVideoDetails(SearchListResponse searchListResponseProcessed)
            throws GeneralSecurityException, IOException, ApiKeyException {
        YouTube youtubeService = getService();
        List<String> videoIds = searchListResponseProcessed.getItems()
                .stream()
                .map(result -> result.getId().getVideoId())
                .collect(Collectors.toList());

        YouTube.Videos.List request = youtubeService.videos().list(VIDEO_DETAILS_FIELDS);
        request.setKey(apiKeyService.getMinigKeyString().get())
                .setMaxResults(100L)
                .setId(String.join(",", videoIds));

        VideoListResponse response;
        try {
            response = request.execute();
        } catch (IOException e) {
            handleApiException(e);
            response = request.execute();
        }

        logResponse(response);
        return response;
    }

    public VideoListResponse mineVideoDetailsByIdList(String idList)
            throws GeneralSecurityException, IOException, ApiKeyException {
        YouTube youtubeService = getService();

        YouTube.Videos.List request = youtubeService.videos().list(VIDEO_DETAILS_FIELDS);
        request.setKey(apiKeyService.getMinigKeyString().get())
                .setMaxResults(101L)
                .setId(idList);

        VideoListResponse response;
        try {
            response = request.execute();
        } catch (IOException e) {
            handleApiException(e);
            response = request.execute();
        }

        logResponse(response);
        return response;
    }


    private void handleApiException(IOException e) throws ApiKeyException {
        if (e instanceof GoogleJsonResponseException && missionControlService.getQuotaExcededMessage().equals(e.getMessage())) {
            log.info("\n\n\n=========================================\n\nRealizando troca de chave de API\n\n=========================================\n\n\n");
            missionControlService.changeMiningApiKey();
        }
    }

    private void logResponse(Object response) {
        if (response != null) {
            log.info(response.toString());
        }
    }




}
