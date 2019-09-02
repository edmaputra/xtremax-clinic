package com.xtremax.clinic.controller;

import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.service.impl.BaseNameService;
import io.github.edmaputra.edtmplte.controller.ABaseController;
import io.github.edmaputra.edtmplte.domain.ABaseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class BaseNameController<T extends ABaseEntity, ID> extends ABaseController<T, ID> {

    private final BaseNameService<T, ID> service;

    public BaseNameController(BaseNameService<T, ID> service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity findByName(@PathVariable("name") String name) throws ResourceNotFoundException {
        T t = service.findByName(name);
        return ResponseEntity.ok().body(t);
    }
}
