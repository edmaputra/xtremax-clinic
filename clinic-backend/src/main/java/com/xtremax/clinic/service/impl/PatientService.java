package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.repository.PatientRepository;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("patientService")
public class PatientService extends BaseNameService<Patient, Long> {

    private final PatientRepository patientRepository;

    public PatientService(@Qualifier("patientRepository") PatientRepository patientRepository) {
        super(patientRepository);
        this.patientRepository = patientRepository;
    }
}
