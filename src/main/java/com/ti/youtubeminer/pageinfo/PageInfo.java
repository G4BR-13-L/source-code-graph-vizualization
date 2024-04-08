package com.ti.youtubeminer.pageinfo;


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
@Table(name = "t_page_info", uniqueConstraints = {
        @UniqueConstraint(name = "uk_page_info_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_page_info", columnNames = {"id"})
})
@SQLDelete(sql = "UPDATE t_page_info SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class PageInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;

    @Column(name = "results_per_page")
    private Integer resultsPerPage;

    @Column(name = "total_results")
    private Integer totalResults;
}
