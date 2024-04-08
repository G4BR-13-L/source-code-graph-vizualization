package com.ti.youtubeminer.searchterm;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISearchTermRepository extends IBaseRepository<SearchTerm> {
    List<SearchTerm> findAllBySearchedIsFalse();
    List<SearchTerm> findAllBySearchedIsTrue();
}
