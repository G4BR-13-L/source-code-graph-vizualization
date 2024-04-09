package com.ti.youtubeminer.video.language;

import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageService extends BaseService<ILanguageRepository, Language> {

    @Autowired
    private ILanguageRepository languageRepository;

    public Language findByCode(String code){
        if(code == null){
            return this.getNoLaguangeDefined();
        }
        Optional<Language> language = languageRepository.findByCode(code);
        if(language.isPresent()){
            return language.get();
        }
        return this.create(code);
    }

    private Language getNoLaguangeDefined() {
        return this.findByCode("language_not_defined");
    }

    private Language create(String code) {
        Language newLanguage = Language.builder()
                .code(code)
                .build();
        return languageRepository.save(newLanguage);
    }
}
