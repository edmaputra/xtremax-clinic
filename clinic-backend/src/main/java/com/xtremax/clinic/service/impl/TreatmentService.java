package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.domain.MedicalPrescription;
import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.domain.Treatment;
import com.xtremax.clinic.domain.handler.TreatmentHandler;
import com.xtremax.clinic.exception.QueueLimitReachedException;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.TreatmentRepository;
import io.github.edmaputra.edtmplte.service.impl.ABaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService extends ABaseServiceImpl<Treatment, Long> {

    private final TreatmentRepository repository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalPrescriptionService medicalPrescriptionService;

    public TreatmentService(TreatmentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Treatment pushQueue(TreatmentHandler handler) throws Exception {
        Date date = handler.getRegistrationTime();
        Doctor doctor = doctorService.retrieveOne(handler.getDoctorId());

        Integer count = repository.countByDoctorAndRegistrationDate(doctor, date);
        System.out.println("Count :"+count);
        if (count > 14)
            throw new QueueLimitReachedException("Patients for this Doctor is Full, please come again next time");

        Patient patient = patientService.retrieveOne(handler.getPatientId());

        Integer number = 0;
        Optional<List<Treatment>> listSaved = repository.findTopByDoctorAndRegistrationDateOrderByNumberDesc(doctor, date);
        if (!listSaved.isPresent()){
            number = 1;
        } else {
            Integer temp = listSaved.get().get(0).getNumber();
            number = temp + 1;
        }

        Treatment queue = new Treatment(patient, doctor, date, date, false);
        queue.setNumber(number);
        Treatment treatment = repository.save(queue);
        return treatment;
    }

    public Treatment callQueue(Long doctorId, Date date) throws Exception, ResourceNotFoundException {
        Doctor doctor = doctorService.retrieveOne(doctorId);
        Optional<Treatment> treatment = repository.findTopByCalledFalseAndDoctorAndRegistrationDateOrderByRegistrationTimeAsc(doctor, date);
        if (!treatment.isPresent()) throw new ResourceNotFoundException("There are no Patients in waiting list");
        Treatment result = treatment.get();
        result.setCalled(true);
        this.add(result);
        return result;
    }

    public List<Treatment> findByPatientAndRegistrationDate(Patient patient, Date registrationDate) throws ResourceNotFoundException {
        Optional<List<Treatment>> treatment = repository.findByPatientAndRegistrationDate(patient, registrationDate);
        if (!treatment.isPresent()) {
            throw new ResourceNotFoundException("Treatment with Patient " + patient.getName() + " and registration date  " + registrationDate + " Not Found");
        }

        return treatment.get();
    }

    public List<Treatment> findByDoctorAndRegistrationDate(Doctor doctor, Date registrationDate) throws ResourceNotFoundException {
        Optional<List<Treatment>> treatment = repository.findByDoctorAndRegistrationDate(doctor, registrationDate);
        if (!treatment.isPresent()) {
            throw new ResourceNotFoundException("Treatment with Doctor " + doctor.getName() + " and registration date  " + registrationDate + " Not Found");
        }

        return treatment.get();
    }

    public List<Treatment> findByDoctorId(Long doctorId) throws Exception, ResourceNotFoundException {
        Doctor doctor = doctorService.retrieveOne(doctorId);
        Optional<List<Treatment>> treatments = repository.findByDoctor(doctor);
        if (!treatments.isPresent()) {
            throw new ResourceNotFoundException("Treatment with Doctor " + doctor.getName() + " Not Found");
        }
        List<Treatment> result = new ArrayList<>();
        List<Treatment> list = treatments.get();
        for (Treatment t: list) {
            Optional<MedicalPrescription> mp = medicalPrescriptionService.findByTreatment(t);
            if (mp.isPresent()) {
                t.setResep(true);
            }
            result.add(t);
        }

        return result;
    }
}
