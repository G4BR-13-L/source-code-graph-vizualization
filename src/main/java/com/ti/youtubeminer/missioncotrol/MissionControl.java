package com.ti.youtubeminer.missioncotrol;


import com.ti.youtubeminer.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_mission_control", uniqueConstraints = {
        @UniqueConstraint(name = "uk_mission_control", columnNames = {"id"}),
        @UniqueConstraint(name = "uk_mission_control_mission_code", columnNames = {"mission_code"}),
})
@ToString(of = {"id", "mission_code"})
public class MissionControl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;

    @Column(name = "mission_code")
    private String missionCode;

    @Column(name = "start_time")
    private long startTime;

}
