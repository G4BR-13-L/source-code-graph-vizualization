package com.ti.youtubeminer.searchterm;


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
@Table(name = "t_search_term", uniqueConstraints = {
        @UniqueConstraint(name = "uk_search_term_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_search_term", columnNames = {"id"}),
        @UniqueConstraint(name = "uk_search_term_term", columnNames = {"term"}),
})
@SQLDelete(sql = "UPDATE t_search_term SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class SearchTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;

    @Column(name = "term")
    private String term;

    @Builder.Default
    @Column(name = "searched")
    private boolean searched = Boolean.FALSE;
}
