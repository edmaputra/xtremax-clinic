package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Patient;
import org.springframework.stereotype.Repository;

@Repository("patientRepository")
public interface PatientRepository extends BaseNameRepository<Patient, Long> {


}
