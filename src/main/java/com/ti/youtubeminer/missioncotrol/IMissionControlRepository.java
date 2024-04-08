package com.ti.youtubeminer.missioncotrol;


import com.ti.youtubeminer.global.domain.repository.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMissionControlRepository extends IBaseRepository<MissionControl> {
    MissionControl findByMissionCode(String missionCode);
}
