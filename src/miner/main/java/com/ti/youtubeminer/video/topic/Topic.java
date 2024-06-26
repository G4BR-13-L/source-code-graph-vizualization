package com.ti.youtubeminer.video.topic;

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
@Table(name = "t_topic", uniqueConstraints = {
        @UniqueConstraint(name = "uk_topic_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_topic_link", columnNames = {"link"}),
        @UniqueConstraint(name = "uk_topic", columnNames = {"id"})
})
@SQLDelete(sql = "UPDATE t_topic SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "link")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_video")
    private Video video;

}