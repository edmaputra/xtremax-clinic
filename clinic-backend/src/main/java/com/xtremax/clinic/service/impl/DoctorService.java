package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.repository.DoctorRepository;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("doctorService")
public class DoctorService extends BaseNameService<Doctor, Long> {

    private final DoctorRepository repository;

    public DoctorService(@Qualifier("doctorRepository") DoctorRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
