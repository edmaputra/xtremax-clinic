package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.service.impl.BaseNameService;
import com.xtremax.clinic.service.impl.MedicineService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicine")
public class MedicineController extends BaseNameController<Medicine, Long> {

    private final MedicineService service;

    public MedicineController(MedicineService service) {
        super(service);
        this.service = service;
    }
}
