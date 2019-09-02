package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.MedicalPrescriptionDetail;
import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.repository.MedicalPrescriptionDetailRepository;
import com.xtremax.clinic.repository.MedicineRepository;
import io.github.edmaputra.edtmplte.service.ABaseService;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MedicalPrescriptionDetailService extends ABaseServiceImpl<MedicalPrescriptionDetail, Long> {

    private final MedicalPrescriptionDetailRepository repository;

    public MedicalPrescriptionDetailService(MedicalPrescriptionDetailRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
