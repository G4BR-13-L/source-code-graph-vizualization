package com.ti.youtubeminer.apikey;


import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_api_key", uniqueConstraints = {
        @UniqueConstraint(name = "uk_api_key_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_api_key_api_key", columnNames = {"api_key"}),
        @UniqueConstraint(name = "uk_api_key", columnNames = {"id"}),
})
@SQLDelete(sql = "UPDATE t_api_key SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class ApiKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "owner")
    private String owner;

    @Builder.Default
    @Column( name = "quota_exceed_at", nullable = false)
    private LocalDateTime quotaExceedAt = LocalDateTime.now().minusDays(1);

    @Builder.Default
    @Column( name = "actual_using")
    private boolean actualUsing = Boolean.FALSE;

}
