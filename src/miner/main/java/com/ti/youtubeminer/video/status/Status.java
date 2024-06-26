package com.ti.youtubeminer.video.status;

import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import com.ti.youtubeminer.video.Video;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_status", uniqueConstraints = {
        @UniqueConstraint(name = "uk_status_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_status", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_status SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Status extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "upload_status")
    private String uploadStatus;

    @Column(name = "privacy_status")
    private String privacyStatus;

    @Column(name = "license")
    private String license;

    @Column(name = "embeddable")
    private Boolean embeddable;

    @Column(name = "public_stats_viewable")
    private Boolean publicStatsViewable;

    @Column(name = "made_for_kids")
    private Boolean madeForKids;

    @OneToOne
    @JoinColumn(name = "fk_video")
    private Video video;

}


