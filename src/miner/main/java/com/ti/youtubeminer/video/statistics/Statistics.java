package com.ti.youtubeminer.video.statistics;

import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import com.ti.youtubeminer.video.Video;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigInteger;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_statistics", uniqueConstraints = {
        @UniqueConstraint(name = "uk_statistics_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_statistics", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_statistics SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Statistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "view_count")
    private BigInteger viewCount;

    @Column(name = "like_count")
    private BigInteger likeCount;

    @Column(name = "favorite_count")
    private BigInteger favoriteCount;

    @Column(name = "comment_count")
    private BigInteger commentCount;

    @Column(name = "video_duration")
    private BigInteger videoDuration;

    @Column(name = "video_duration_original_string")
    private String videoDurationOriginalString;

    @OneToOne(mappedBy = "statistics")
    @JoinColumn(name = "fk_video")
    private Video video;

}
