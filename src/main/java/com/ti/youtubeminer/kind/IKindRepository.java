package com.ti.youtubeminer.kind;

import com.ti.youtubeminer.global.domain.repository.IBaseRepository;

public interface IKindRepository extends IBaseRepository<Kind> {
    Kind findByDescription(String description);
}
