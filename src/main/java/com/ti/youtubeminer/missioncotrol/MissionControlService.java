package com.ti.youtubeminer.missioncotrol;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.ti.youtubeminer.apikey.ApiKey;
import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.global.domain.exceptions.ApiKeyException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class MissionControlService {

    @Autowired
    private IMissionControlRepository missionControlRepository;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private EnvironmentChecker environmentChecker;


    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "YouTube Miner";
    private static final String VIDEO_TYPE = "video";
    private static final String VIDEO_DETAILS_FIELDS = "snippet,statistics,status,topicDetails";
    private static final String MISSION_CODE = "YOUTUBE_MINER";
    private static final String QUOTA_EXCEDED_MESSAGE = "The request cannot be completed because you have exceeded your <a href=\\\"/youtube/v3/getting-started#quota\\\">quota</a>.";
    private static final String COMMENTS_DISABLED = "The video identified by the <code><a href=\\\"/youtube/v3/docs/commentThreads/list#videoId\\\">videoId</a></code> parameter has disabled comments.";

    public String getQuotaExcededMessage() {
        return QUOTA_EXCEDED_MESSAGE;
    }

    public String getCommentsDisabled() {
        return COMMENTS_DISABLED;
    }

    public String getMissionCode() {
        return MISSION_CODE;
    }

    public void init() throws ApiKeyException {
        this.clean();
        this.prepareKeysToMining();
        this.selectFirstKey();
        MissionControl missionControl = MissionControl.builder()
                .startTime(System.currentTimeMillis())
                .missionCode(MISSION_CODE)
                .build();
        missionControlRepository.save(missionControl);
    }

    public MissionControl getMissionControl() {
        return missionControlRepository.findByMissionCode(MISSION_CODE);
    }

    public void clean() {
        missionControlRepository.deleteAll();
    }

    public void changeMiningApiKey() throws ApiKeyException {

        this.expireMiningApiKey();

        List<ApiKey> apiKeys = apiKeyService.findAll();

        apiKeys.stream().forEach(a -> a.setActualUsing(false));
        apiKeyService.saveAll(apiKeys);

        Optional<ApiKey> theNewApiKey = apiKeys.stream().min((a, b) -> a.getQuotaExceedAt().compareTo(b.getQuotaExceedAt()));
        if (theNewApiKey.isPresent()) {
            LocalDateTime quotaExceedAt = theNewApiKey.get().getQuotaExceedAt();
            LocalDateTime currentTime = LocalDateTime.now();

            Duration duration = Duration.between(quotaExceedAt, currentTime);
            long hoursDifference = duration.toHours();

            if (hoursDifference >= 24) {
                theNewApiKey.get().setActualUsing(true);
                apiKeyService.save(theNewApiKey.get());
            } else {
                throw new ApiKeyException("" +
                        "\n\n\n\n--------------------------------------------------------------------------\n\n" +
                        "Mieração interrompida por todos as chaves de API terem suas cotas excedidas no google cloud.\n" +
                        "Nesse caso, é preciso aguardar 24 horas \n\n" +
                        "--------------------------------------------------------------------------\n\n\n\n");
            }
        }
    }

    private void expireMiningApiKey() {
        Optional<ApiKey> expiredApiKey = apiKeyService.getMinigKey();
        if (expiredApiKey.isPresent()) {
            expiredApiKey.get().setActualUsing(false);
            expiredApiKey.get().setQuotaExceedAt(LocalDateTime.now());
            apiKeyService.save(expiredApiKey.get());
        }
    }

    public void prepareKeysToMining() {
        List<ApiKey> apiKeys = apiKeyService.findAll();
        apiKeys.stream().forEach(a -> {
            a.setActualUsing(false);
            a.setQuotaExceedAt(LocalDateTime.now().minusDays(2));
        });
        apiKeyService.saveAll(apiKeys);
    }

    public void printUsedApiKey() {
        System.out.println("\n=========================== Chave de API utilizada ===========================\n\n"
                + apiKeyService.getMinigKeyString() + "\n\n===========================================================================\n\n");
    }

    public void selectFirstKey() throws ApiKeyException {
        ApiKey apiKey = apiKeyService.findFirst();
        apiKey.setActualUsing(true);
        apiKeyService.save(apiKey);
        if (!environmentChecker.isTestEnvironment()) {
            try {
                testApiKey(apiKey);
            } catch (Exception e) {
                this.changeMiningApiKey();
            }
        }
    }

    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private SearchListResponse testApiKey(ApiKey apiKey)
            throws GeneralSecurityException, IOException, ApiKeyException {
        YouTube youtubeService = getService();
        YouTube.Search.List request = youtubeService.search().list("snippet");
        request.setKey(apiKey.getApiKey())
                .setMaxResults(1L)
                .setQ("Teste")
                .setType(VIDEO_TYPE);

        return request.execute();
    }


}
