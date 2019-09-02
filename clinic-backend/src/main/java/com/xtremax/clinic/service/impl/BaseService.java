package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {

    private final BaseRepository<T, ID> repository;

    public BaseService(BaseRepository<T, ID> baseRepository) {
        this.repository = baseRepository;
    }

    public List<T> findAll() throws ResourceNotFoundException {
        List<T> ts = repository.findAll();
        if (ts.isEmpty()) {
            throw new ResourceNotFoundException("Data Empty");
        }
        return ts;
    }

    public T findOne(ID id) throws ResourceNotFoundException {
        return  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource no found with id "+ id+", Please Register via Administrator"));
    }

    public T save(T t) {
        return repository.save(t);
    }
}
