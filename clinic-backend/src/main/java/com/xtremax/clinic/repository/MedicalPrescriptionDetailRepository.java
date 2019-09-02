package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.MedicalPrescription;
import com.xtremax.clinic.domain.MedicalPrescriptionDetail;
import io.github.edmaputra.edtmplte.repository.ABaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalPrescriptionDetailRepository extends ABaseRepository<MedicalPrescriptionDetail, Long> {

}
