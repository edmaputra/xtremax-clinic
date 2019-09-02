package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.repository.MedicineRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicineService extends BaseNameService<Medicine, Long> {

    private final MedicineRepository repository;

    public MedicineService(MedicineRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
