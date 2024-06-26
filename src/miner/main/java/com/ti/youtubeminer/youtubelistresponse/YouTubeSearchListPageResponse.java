package com.ti.youtubeminer.youtubelistresponse;

import com.ti.youtubeminer.enums.ContentTypeEnum;
import com.ti.youtubeminer.enums.ProgrammingLanguageEnum;
import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import com.ti.youtubeminer.kind.Kind;
import com.ti.youtubeminer.pageinfo.PageInfo;
import com.ti.youtubeminer.video.Video;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_youtube_list_response", uniqueConstraints = {
        @UniqueConstraint(name = "uk_youtube_list_response_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_youtube_list_response", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_youtube_list_response SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class YouTubeSearchListPageResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;

    @Column(name = "next_page_token")
    private String nextPageToken;

    @Builder.Default
    @Column(name = "completely_processed")
    private boolean completelyProcessed = Boolean.FALSE;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "prev_page_token")
    private String prevPageToken;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "visitor_id")
    private String visitorId;

    @Column(name = "etag")
    private String etag;

    @ManyToOne
    @JoinColumn(name = "fk_kind")
    private Kind kind;

    @OneToOne
    @JoinColumn(name = "fk_page_info")
    private PageInfo pageInfo;

    @Column(name = "content_type", columnDefinition = ContentTypeEnum.CONTENT_TYPE_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType;

    @Column(name = "programming_language", columnDefinition = ProgrammingLanguageEnum.PROGRAMMING_LANGUAGE_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private ProgrammingLanguageEnum programmingLanguage = ProgrammingLanguageEnum.NOT_SPECIFIED;

    @OneToMany(mappedBy = "originListResponse", cascade = CascadeType.MERGE)
    private List<Video> videos;
}
