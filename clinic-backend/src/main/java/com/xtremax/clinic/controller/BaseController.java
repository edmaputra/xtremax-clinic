package com.xtremax.clinic.controller;

import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.service.impl.BaseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class BaseController<T, ID> {

    private final BaseService<T, ID> service;

    public BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity findAll() throws ResourceNotFoundException {
        List<T> ts = service.findAll();
        return ResponseEntity.ok().body(ts);
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable("id") ID id) throws ResourceNotFoundException {
        T t = service.findOne(id);
        return ResponseEntity.ok().body(t);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@RequestBody T t) {
        T t1 = service.save(t);
        return ResponseEntity.ok().body(t);
    }
}
