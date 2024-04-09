package com.ti.youtubeminer.video;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVideoRepository extends IBaseRepository<Video> {

    Optional<Video> findFirstByVideoId(String videoId);

    List<Video> findByVideoIdIn(List<String> videoId);

    @Query("SELECT COUNT(DISTINCT v.videoId) FROM Video v")
    Long countUniqueVideos();

    @Query(value = "SELECT DISTINCT ON (video_id) * FROM t_video where mined_details = false ORDER BY video_id ASC LIMIT 10000", nativeQuery = true)
    List<Video> findAllUniqueVideos();

    List<Video> findAllByVideoId(String id);

    List<Video> findAllByVideoIdIn(List<String> videoIds);

    @Query(value = "SELECT COUNT(*) FROM t_video where mined_details = true", nativeQuery = true)
    Long countMinedDetatilsVideos();

    @Query(value = "SELECT COUNT(*) FROM t_video where valid_by_miner = false", nativeQuery = true)
    Long countInvalidVideos();

    @Query(value = "SELECT COUNT(*) FROM t_video where removed_from_youtube = true", nativeQuery = true)
    Long countRemovedVideos();
}
