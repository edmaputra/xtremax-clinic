package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.service.impl.DoctorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController extends BaseNameController<Doctor, Long> {

    private final DoctorService service;

    public DoctorController(DoctorService service) {
        super(service);
        this.service = service;
    }

}
