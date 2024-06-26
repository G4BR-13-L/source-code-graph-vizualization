package com.ti.youtubeminer.databaseloader;

import com.ti.youtubeminer.global.domain.exceptions.EntityNullException;
import com.ti.youtubeminer.utils.ValidationUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class DatabaseLoaderService {

    @Autowired
    private IDatabaseLoaderRepository databaseLoaderRepository;

    public boolean loadEntity(String entidade) {
        DatabaseLoader databaseLoader = databaseLoaderRepository.findByEntity(entidade).orElse(null);
        return ValidationUtil.isNull(databaseLoader) || databaseLoader.getDoDump();
    }

    public void save(String entidade) {
        if (ValidationUtil.isBlank(entidade)) {
            throw new EntityNullException("Nenhuma entidade foi informada para ser cadastrada");
        }
        DatabaseLoader databaseLoader = databaseLoaderRepository.findByEntity(entidade).orElse(DatabaseLoader.builder().entity(entidade).build());
        databaseLoader.setDoDump(false);
        databaseLoaderRepository.save(databaseLoader);
    }
}