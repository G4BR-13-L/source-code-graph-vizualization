package com.ti.youtubeminer.apikey;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IApiKeyRepository extends IBaseRepository<ApiKey> {

    Optional<ApiKey> findFirstByActualUsingIsTrue();
}
