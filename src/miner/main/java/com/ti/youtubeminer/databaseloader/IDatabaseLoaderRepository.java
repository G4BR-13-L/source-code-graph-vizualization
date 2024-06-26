package com.ti.youtubeminer.databaseloader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDatabaseLoaderRepository extends JpaRepository<DatabaseLoader, Long> {
    Optional<DatabaseLoader> findByEntity(String entity);
}
