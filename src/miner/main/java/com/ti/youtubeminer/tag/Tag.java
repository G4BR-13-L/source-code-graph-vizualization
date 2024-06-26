package com.ti.youtubeminer.tag;

import com.ti.youtubeminer.global.domain.entity.BaseEntity;
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
@Table(name = "t_tag", uniqueConstraints = {
        @UniqueConstraint(name = "uk_tag_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_tag_label", columnNames = {"label"}),
        @UniqueConstraint(name = "uk_tag", columnNames = {"id"})
})
@SQLDelete(sql = "UPDATE t_tag SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name="label", nullable = false)
    private String label;
}
