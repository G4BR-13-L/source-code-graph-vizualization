package com.ti.youtubeminer.youtubelistresponse;


import com.ti.youtubeminer.enums.ContentTypeEnum;
import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeSearchListPageResponseService extends BaseService<IYouTubeSearchListPageResponseRepository, YouTubeSearchListPageResponse> {

    @Autowired
    private IYouTubeSearchListPageResponseRepository youTubeListResponseRepository;


    public YouTubeSearchListPageResponse findLatestByContentType(ContentTypeEnum contentType) {
        return youTubeListResponseRepository.findLatestByContentType(contentType);
    }

    public YouTubeSearchListPageResponse findLatestByContentTypeWhereCompletelyProcessedIsTrue(ContentTypeEnum contentTypeEnum){
        return youTubeListResponseRepository.findLatestByContentTypeWhereCompletelyProcessedIsTrue(contentTypeEnum);
    }
    public boolean weDoNotHavePages() {
        return youTubeListResponseRepository.findAll().isEmpty();
    }

    public boolean existsByEtag(String etag) {
        return youTubeListResponseRepository.existsByEtag(etag);
    }

    public boolean existsByNextPageToken(String nextPageToken) {
        return  youTubeListResponseRepository.existsByNextPageToken(nextPageToken);
    }
}
