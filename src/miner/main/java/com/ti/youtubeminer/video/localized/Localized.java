package com.ti.youtubeminer.video.localized;

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
@Table(name = "t_localized", uniqueConstraints = {
        @UniqueConstraint(name = "uk_localized_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_localized", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_localized SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Localized extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_video")
    private Video video;

}
