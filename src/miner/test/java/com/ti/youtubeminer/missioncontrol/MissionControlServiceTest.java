package com.ti.youtubeminer.missioncontrol;

import com.ti.youtubeminer.apikey.ApiKey;
import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.missioncotrol.MissionControlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MissionControlServiceTest {

    @Autowired
    private MissionControlService missionControlService;

    @Autowired
    private ApiKeyService apiKeyService;

//    @Test
//    public void testChangeAndSelectMiningApiKey() {
//        try {
//            missionControl.changeMiningApiKey();
//        } catch (ApiKeyException e) {
//            throw new RuntimeException(e);
//        }
//        Optional<ApiKey> actualKey = apiKeyService.getMinigKey();
//        assertThat(actualKey.isPresent()).isTrue();
//    }
//
//    @Test
//    public void testChangeMiningApiKey() throws ApiKeyException {
//        missionControl.changeMiningApiKey();
//        Optional<ApiKey> actualKey = apiKeyService.getMinigKey();
//        missionControl.changeMiningApiKey();
//        Optional<ApiKey> newKey = apiKeyService.getMinigKey();
//
//        assertThat(actualKey.isPresent()).isTrue();
//        assertThat(newKey.isPresent()).isTrue();
//        assertThat(newKey.get().getApiKey()).isNotEqualTo(actualKey.get().getApiKey());
//    }

    @Test
    public void testPrepareKeysToMining() {
        missionControlService.prepareKeysToMining();
        List<ApiKey> apiKeys = apiKeyService.findAll();

        assertThat(apiKeys).isNotEmpty();
        assertThat(apiKeys).allMatch(apiKey -> !apiKey.isActualUsing());

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        assertThat(apiKeys).allMatch(apiKey -> apiKey.getQuotaExceedAt().isBefore(twoDaysAgo));
    }
}
