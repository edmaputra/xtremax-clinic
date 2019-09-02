package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Treatment;
import com.xtremax.clinic.domain.handler.TreatmentHandler;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.service.impl.TreatmentService;
import io.github.edmaputra.edtmplte.controller.ABaseController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/queue/doctor")
public class TreatmentController extends ABaseController<Treatment, Long> {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{doctorId}/call")
    public ResponseEntity call(
            @PathVariable("doctorId") String doctorId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws Exception, ResourceNotFoundException {
        Treatment treatment = service.callQueue(Long.valueOf(doctorId), date);
        return ResponseEntity.ok().body(treatment);
    }

    @GetMapping("/{doctorId}/list")
    public ResponseEntity list(
            @PathVariable("doctorId") String doctorId
    ) throws Exception, ResourceNotFoundException {
        List<Treatment> treatments = service.findByDoctorId(Long.valueOf(doctorId));
        return ResponseEntity.ok().body(treatments);
    }

    @PostMapping("/push")
    public ResponseEntity registration(@RequestBody TreatmentHandler handler) throws Exception {
        Treatment queue = service.pushQueue(handler);
        return  ResponseEntity.ok().body(queue);
    }

}
