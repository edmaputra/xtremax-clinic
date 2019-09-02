package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.handler.MedicalPrescriptionHandler;
import com.xtremax.clinic.domain.MedicalPrescription;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.service.impl.MedicalPrescriptionService;
import io.github.edmaputra.edtmplte.controller.ABaseController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/queue/prescription")
public class MedicalPresciptionController extends ABaseController<MedicalPrescription, Long> {

    private final MedicalPrescriptionService service;

    public MedicalPresciptionController(MedicalPrescriptionService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/push")
    public ResponseEntity pushQueue(@RequestBody MedicalPrescriptionHandler handler) throws Exception, ResourceNotFoundException {
        MedicalPrescription mp = service.pushQueue(handler);
        return ResponseEntity.ok().body(mp);
    }

    @GetMapping("/done")
    public ResponseEntity done(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm") Date date) throws ResourceNotFoundException {
        List<MedicalPrescription> list = service.findAllFinished(date);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity accept(@PathVariable("id") Long id) throws Exception {
        MedicalPrescription md = service.setDoneTime(id);
        return ResponseEntity.ok().body(md);
    }

}
