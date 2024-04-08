package com.ti.youtubeminer.video;

import com.ti.youtubeminer.enums.AnalyzedAsEnum;
import com.ti.youtubeminer.enums.ProgrammingLanguageEnum;
import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import com.ti.youtubeminer.kind.Kind;
import com.ti.youtubeminer.tag.Tag;
import com.ti.youtubeminer.video.language.Language;
import com.ti.youtubeminer.video.localized.Localized;
import com.ti.youtubeminer.video.statistics.Statistics;
import com.ti.youtubeminer.video.status.Status;
import com.ti.youtubeminer.video.topic.Topic;
import com.ti.youtubeminer.youtubelistresponse.YouTubeSearchListPageResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_video", uniqueConstraints = {
        @UniqueConstraint(name = "uk_video_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_video", columnNames = {"id"}),
//        @UniqueConstraint(name = "uk_video_youtube_id", columnNames = {"video_id"})
})
@SQLDelete(sql = "UPDATE t_video SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "videoId", "title"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "published_at")
    private Date publishedAt;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "title", columnDefinition = "VARCHAR(512)")
    private String title;

    @Column(name = "channel_title")
    private String channelTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Tag> tags;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "live_broadcast_content")
    private String liveBroadcastContent;

    @OneToOne(mappedBy = "video", cascade = CascadeType.MERGE)
    private Localized localized;

    @ManyToOne
    private Language defaultAudioLanguage;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_status")
    private Status status;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_statistics")
    private Statistics statistics;

    @OneToMany(mappedBy = "video", cascade = CascadeType.MERGE)
    private List<Topic> topics;

    @Column(name = "url")
    private String url;

    @Column(name = "etag")
    private String etag;

    @ManyToOne
    @JoinColumn(name = "fk_kind")
    private Kind kind;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "programming_language", columnDefinition = ProgrammingLanguageEnum.PROGRAMMING_LANGUAGE_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private ProgrammingLanguageEnum programmingLanguage = ProgrammingLanguageEnum.NOT_SPECIFIED;

    @Column(name = "analyzed_as", columnDefinition = AnalyzedAsEnum.ANALYZED_AS_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private AnalyzedAsEnum analyzedAs = AnalyzedAsEnum.PROGRAMMING_LANGUAGE_NOT_IDENTIFIED;

    @Builder.Default
    @Column(name = "mined_details")
    private boolean minedDetails = Boolean.FALSE;

    @Builder.Default
    @Column(name = "valid_by_miner", nullable = false)
    private boolean validByMiner = Boolean.TRUE;

    @Builder.Default
    @Column(name = "removed_from_youtube", nullable = false)
    private boolean removedFromYouTube = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_origin_list_response")
    private YouTubeSearchListPageResponse originListResponse;
}
