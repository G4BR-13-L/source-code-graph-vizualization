package com.ti.youtubeminer.youtubelistresponse;

import com.ti.youtubeminer.enums.ContentTypeEnum;
import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IYouTubeSearchListPageResponseRepository extends IBaseRepository<YouTubeSearchListPageResponse> {

    @Query("SELECT r FROM YouTubeSearchListPageResponse r " +
            "WHERE r.contentType = :contentType " +
            "ORDER BY r.id DESC LIMIT 1")
    YouTubeSearchListPageResponse findLatestByContentType(
            @Param("contentType") ContentTypeEnum contentType
    );


    @Query("SELECT r FROM YouTubeSearchListPageResponse r " +
            "WHERE r.contentType = :contentType " +
            "AND r.completelyProcessed = true " +
            "ORDER BY r.id DESC LIMIT 1")
    YouTubeSearchListPageResponse findLatestByContentTypeWhereCompletelyProcessedIsTrue(
            @Param("contentType") ContentTypeEnum contentType
    );

    boolean existsByEtag(String etag);

    boolean existsByNextPageToken(String nextPageToken);
}
