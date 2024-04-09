package com.ti.youtubeminer.databaseloader;

import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_database_loader", uniqueConstraints = {
        @UniqueConstraint(name = "uk_database_loader_hash_id", columnNames = {"hash_id"}),
        @UniqueConstraint(name = "uk_database_loader", columnNames = {"entity"})
})
@SQLDelete(sql = "UPDATE t_database_loader SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@ToString(of = {"id", "hashId"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class DatabaseLoader extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;

    @Column(name = "entity", nullable = false)
    private String entity;

    @Builder.Default
    @Column(name = "do_dump", nullable = false)
    private Boolean doDump = Boolean.FALSE;
}