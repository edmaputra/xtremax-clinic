package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.MedicalPrescription;
import com.xtremax.clinic.domain.MedicalPrescriptionDetail;
import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.domain.Treatment;
import com.xtremax.clinic.domain.handler.MedicalPrescriptionHandler;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.MedicalPrescriptionRepository;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalPrescriptionService extends ABaseServiceImpl<MedicalPrescription, Long> {

    private final MedicalPrescriptionRepository repository;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private TreatmentService treatmentService;

    @Autowired
    private PatientService patientService;

    public MedicalPrescriptionService(MedicalPrescriptionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public MedicalPrescription pushQueue(MedicalPrescriptionHandler handler) throws Exception, ResourceNotFoundException {

        Treatment treatment = treatmentService.retrieveOne(handler.getTreatmentId());

        List<MedicalPrescriptionDetail> details = new ArrayList<>();

        for (int i = 0; i < handler.getDetails().size(); i++) {
            MedicalPrescriptionDetail d = new MedicalPrescriptionDetail();
            Medicine med = medicineService.retrieveOne(handler.getDetails().get(i).getMedicineId());
            d.setAmount(handler.getDetails().get(i).getAmount());
            d.setMedicine(med);
            details.add(d);
        }

        MedicalPrescription mp = new MedicalPrescription();
        mp.setTreatment(treatment);
        mp.setDetails(details);
        mp.setRegisteredAt(handler.getTime());

        MedicalPrescription result = repository.save(mp);
        return result;
    }

    public MedicalPrescription setDoneTime(Long id) throws Exception {
        Optional<MedicalPrescription> mp = repository.findById(id);
        if (!mp.isPresent()){
            throw new ResourceNotFoundException("Medical Prescription with id:"+id+" Not Found");
        }
        MedicalPrescription get = mp.get();
        get.setDoneAt(this.setDoneAt(new Date(), get.getDetails()));
        repository.save(get);
        return get;

    }

    public List<MedicalPrescription> findAllFinished(Date date) throws ResourceNotFoundException {
        Optional<List<MedicalPrescription>> medicalPrescriptions = repository.findAllFinished(date);
        if (!medicalPrescriptions.isPresent()) {
            throw new ResourceNotFoundException("Medical Prescription not found");
        }

        return medicalPrescriptions.get();
    }

    public Optional<MedicalPrescription> findByTreatment(Treatment treatment){
        return repository.findByTreatment(treatment);
    }

    private Date setDoneAt(Date doneAt, List<MedicalPrescriptionDetail> medicines) {
        DateTime dateTime = new DateTime(doneAt);
        Integer secondsAppend = 0;
        for(MedicalPrescriptionDetail d: medicines) {
            Integer temp = d.getAmount() * d.getMedicine().getProcessDuration();
            secondsAppend += temp;
        }
        dateTime = dateTime.plusSeconds(secondsAppend);
        return dateTime.toDate();
    }
}
