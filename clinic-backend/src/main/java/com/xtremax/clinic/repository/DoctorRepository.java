package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Doctor;
import org.springframework.stereotype.Repository;

@Repository("doctorRepository")
public interface DoctorRepository extends  BaseNameRepository<Doctor, Long> {


}
