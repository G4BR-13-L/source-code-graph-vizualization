package com.ti.youtubeminer.apikey;

import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyService extends BaseService<IApiKeyRepository, ApiKey> {

    @Autowired
    private IApiKeyRepository apiKeyRepository;

    public Optional<String> getMinigKeyString(){
        Optional<ApiKey> apiKey = apiKeyRepository.findFirstByActualUsingIsTrue();
        if ( apiKey.isEmpty() ){
            return Optional.of(null);
        }
        return Optional.of(apiKey.get().getApiKey());
    }

    public Optional<ApiKey> getMinigKey(){
        return apiKeyRepository.findFirstByActualUsingIsTrue();
    }

    public ApiKey findFirst(){
        return apiKeyRepository.findAll().get(0);
    }
}
