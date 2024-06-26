package com.ti.youtubeminer.kind;

import com.ti.youtubeminer.enums.ContentTypeEnum;
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
@Table(name = "t_kind", uniqueConstraints = {
        @UniqueConstraint(name = "uk_kind_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_kind_description", columnNames = {"description"}),
        @UniqueConstraint(name = "uk_kind", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_kind SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Kind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name = "content_type", columnDefinition = ContentTypeEnum.CONTENT_TYPE_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType;

    @Column(name="kind", columnDefinition = KindEnum.KIND_NULL_CHECK_CONSTRAINT)
    @Enumerated(EnumType.STRING)
    private KindEnum kind;
}
