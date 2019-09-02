package com.xtremax.clinic.service;

import com.xtremax.clinic.exception.ResourceNotFoundException;
import io.github.edmaputra.edtmplte.domain.ABaseEntity;
import io.github.edmaputra.edtmplte.service.ABaseService;

public interface NameService<T extends ABaseEntity, ID> extends ABaseService<T, ID> {

    T findByName(String name) throws ResourceNotFoundException;

}
