package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.BaseNameRepository;
import com.xtremax.clinic.repository.BaseRepository;
import com.xtremax.clinic.service.NameService;
import io.github.edmaputra.edtmplte.domain.ABaseEntity;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;

import java.util.Optional;

public abstract class BaseNameService<T extends ABaseEntity, ID> extends ABaseServiceImpl<T, ID> implements NameService<T, ID> {

    private final BaseNameRepository<T, ID> repository;

    public BaseNameService(BaseNameRepository<T, ID> repository) {
        super(repository);
        this.repository = repository;
    }

    public T findByName(String name) throws ResourceNotFoundException {
        Optional<T> name1 = repository.findByName(name);
        if(!name1.isPresent()) {
            throw new ResourceNotFoundException("Entity with name "+ name +" not found");
        }

        return name1.get();
    }
}
