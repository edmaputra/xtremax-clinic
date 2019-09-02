package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.service.impl.PatientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController extends BaseNameController<Patient, Long> {

    private final PatientService service;

    public PatientController(PatientService service) {
        super(service);
        this.service = service;
    }

}
