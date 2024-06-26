package com.ti.youtubeminer.video.language;

import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import com.ti.youtubeminer.kind.KindEnum;
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
@Table(name = "t_language", uniqueConstraints = {
        @UniqueConstraint(name = "uk_language_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_language_code", columnNames = {"code"}),
        @UniqueConstraint(name = "uk_language", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_kind SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Language extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name="code", nullable = false)
    private String code;

}
