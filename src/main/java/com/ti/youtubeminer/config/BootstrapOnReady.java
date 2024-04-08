package com.ti.youtubeminer.config;

import com.google.gson.reflect.TypeToken;
import com.ti.youtubeminer.apikey.ApiKey;
import com.ti.youtubeminer.apikey.ApiKeyService;
import com.ti.youtubeminer.databaseloader.DatabaseLoaderService;
import com.ti.youtubeminer.kind.Kind;
import com.ti.youtubeminer.kind.KindService;
import com.ti.youtubeminer.searchterm.SearchTerm;
import com.ti.youtubeminer.searchterm.SearchTermService;
import com.ti.youtubeminer.utils.JsonUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

@Log
@Configuration
public class BootstrapOnReady {

    private static final String PATH_JSON_API_KEYS = "/databaseloader/api_key.json";
    private static final String PATH_JSON_KIND = "/databaseloader/kind.json";
    private static final String PATH_JSON_SEARCH_TERM = "/databaseloader/search_term.json";

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private KindService kindService;

    @Autowired
    private SearchTermService searchTermService;

    @Autowired
    private DatabaseLoaderService databaseLoaderService;

    @EventListener(value = ApplicationReadyEvent.class)
    public void bootstraper() {
        List<ApiKey> apikeys = JsonUtil.objectListFromJson("api_key", PATH_JSON_API_KEYS, new TypeToken<ArrayList<ApiKey>>() {
        }.getType());

        List<Kind> kinds = JsonUtil.objectListFromJson("kind", PATH_JSON_KIND, new TypeToken<ArrayList<Kind>>() {
        }.getType());

        List<SearchTerm> searchTerms = JsonUtil.objectListFromJson("search_term", PATH_JSON_SEARCH_TERM, new TypeToken<ArrayList<SearchTerm>>() {
        }.getType());

        if ((databaseLoaderService.loadEntity(ApiKey.class.getSimpleName()))) {
            apikeys.forEach(this::createApiKey);
            databaseLoaderService.save(ApiKey.class.getSimpleName());
        }

        if ((databaseLoaderService.loadEntity(Kind.class.getSimpleName()))) {
            kinds.forEach(this::createKind);
            databaseLoaderService.save(Kind.class.getSimpleName());
        }

        if ((databaseLoaderService.loadEntity(SearchTerm.class.getSimpleName()))) {
            searchTerms.forEach(this::createSearchTerm);
            databaseLoaderService.save(Kind.class.getSimpleName());
        }
    }


    private void createApiKey(ApiKey apiKey) {
        try {
            apiKeyService.save(apiKey);
        } catch (Exception exception) {
            log.severe("A chave de Api " + apiKey.getApiKey() + " não pode ser cadastrada. Erro: " + exception.getMessage());
        }
    }

    private void createKind(Kind kind) {
        try {
            kindService.save(kind);
        } catch (Exception exception) {
            log.severe("O kind " + kind.getDescription() + " não pode ser cadastrado. Erro: " + exception.getMessage());
        }
    }

    private void createSearchTerm(SearchTerm searchTerm) {
        try {
            searchTermService.save(searchTerm);
        } catch (Exception exception) {
            log.severe("O termo de pesquisa " + searchTerm.getTerm() + " não pode ser cadastrado. Erro: " + exception.getMessage());
        }
    }

}