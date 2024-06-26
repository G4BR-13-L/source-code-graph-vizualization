package com.ti.youtubeminer.pageinfo;


import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageInfoService extends BaseService<IPageInfoRepository, PageInfo> {
    @Autowired
    private IPageInfoRepository pageInfoRepository;
}
