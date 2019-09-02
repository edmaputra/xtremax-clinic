package com.xtremax.clinic.service.impl;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.repository.DoctorRepository;
import com.xtremax.clinic.repository.MedicineRepository;
import com.xtremax.clinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        patientRepository.saveAll(Arrays.asList(
                new Patient("patient1"),
                new Patient("patient2"),
                new Patient("patient3")
        ));

        doctorRepository.saveAll(Arrays.asList(
                new Doctor("doctor1"),
                new Doctor("doctor2"),
                new Doctor("doctor3")
        ));

        medicineRepository.saveAll(Arrays.asList(
                new Medicine("medicine1", 60),
                new Medicine("medicine2", 120),
                new Medicine("medicine3", 180),
                new Medicine("medicine4", 240),
                new Medicine("medicine5", 300)

        ));
    }
}
