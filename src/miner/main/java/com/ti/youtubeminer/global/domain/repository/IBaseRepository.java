package com.ti.youtubeminer.global.domain.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IBaseRepository<E> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
    boolean existsByHashId(String hashId);

    Optional<E> findByHashId(String hashId);

    @Transactional
    Long deleteByHashId(String hashId);

    @Transactional
    List<E> removeByHashId(String hashId);

}
