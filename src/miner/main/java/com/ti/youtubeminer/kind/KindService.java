package com.ti.youtubeminer.kind;


import com.ti.youtubeminer.global.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KindService extends BaseService<IKindRepository, Kind> {

    @Autowired
    private IKindRepository kindRepository;

    public Kind findByDescription(String description){
        return kindRepository.findByDescription(description);
    }
}
