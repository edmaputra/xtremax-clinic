package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.MedicalPrescription;
import com.xtremax.clinic.domain.Treatment;
import io.github.edmaputra.edtmplte.repository.ABaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalPrescriptionRepository extends ABaseRepository<MedicalPrescription, Long> {

    Optional<MedicalPrescription> findByTreatment(Treatment treatment);
//
//    @Query("SELECT a FROM MedicalPrescription a WHERE a.treatment.id = :id")
//    Optional<MedicalPrescription> findByTreatmentId(@Param("id") Integer id);

    @Query("SELECT a FROM MedicalPrescription a WHERE a.doneAt <= :now")
    Optional<List<MedicalPrescription>> findAllFinished(@Param("now") Date now);

}
