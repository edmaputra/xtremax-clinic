package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.domain.Treatment;
import io.github.edmaputra.edtmplte.repository.ABaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentRepository extends ABaseRepository<Treatment, Long> {

    Optional<List<Treatment>> findTopByDoctorAndRegistrationDateOrderByNumberDesc(Doctor doctor, Date registrationDate);

    Optional<Treatment> findTopByCalledFalseAndDoctorAndRegistrationDateOrderByRegistrationTimeAsc(Doctor doctor, Date registrationDate);

    Integer countByDoctorAndRegistrationDate(Doctor doctor, Date date);

    Optional<List<Treatment>> findByDoctor(Doctor doctor);

    Optional<List<Treatment>> findByPatientAndRegistrationDate(Patient patient, Date registrationDate);

    Optional<List<Treatment>> findByDoctorAndRegistrationDate(Doctor doctor, Date registrationDate);
}
