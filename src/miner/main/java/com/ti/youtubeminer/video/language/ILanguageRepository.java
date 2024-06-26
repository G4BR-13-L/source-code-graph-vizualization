package com.ti.youtubeminer.video.language;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILanguageRepository extends IBaseRepository<Language> {
    Optional<Language> findByCode(String code);
}
