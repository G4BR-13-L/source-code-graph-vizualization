package com.ti.youtubeminer.searchterm;

import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SearchTermService extends BaseService<ISearchTermRepository, SearchTerm> {

    @Autowired
    private ISearchTermRepository searchTermRepository;

    public List<SearchTerm> findAllUnsearched() {
        return searchTermRepository.findAllBySearchedIsFalse();
    }
    public int countAllSearched() {
        return searchTermRepository.findAllBySearchedIsTrue().size();
    }
}
