package com.ti.youtubeminer.global.domain.service;


import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService<R extends IBaseRepository<E>, E> {

    @Autowired
    private R repository;

    public E save(E entity){
        return repository.save(entity);
    }

    public void saveAll(List<E> entities) {
        repository.saveAll(entities);
    }

    public long count(){
        return repository.count();
    }

    public List<E> findAll() {
        return repository.findAll();
    }
}
