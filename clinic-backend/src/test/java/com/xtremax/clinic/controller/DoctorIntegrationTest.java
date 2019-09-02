package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DoctorIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String name = "bangun";

    @Test
    public void whenSaveNewEntity_shouldReturnCorrectEntity() {
        Doctor doctor = new Doctor(name);
        ResponseEntity<Doctor> responseEntity = restTemplate.postForEntity("/doctor", doctor, Doctor.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldReturnCorrectEntity() {
        Doctor doctor = new Doctor(name);
        restTemplate.postForEntity("/doctor", doctor, Doctor.class);

        ResponseEntity<Doctor> responseEntity = restTemplate.getForEntity("/doctor/name/"+name, Doctor.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldThrowException() throws ResourceNotFoundException {
        restTemplate.getForEntity("/doctor/name/"+name, String.class);
    }

    @Test
    public void whenFindAll_shouldReturnListEntity() {
        Doctor doctor1 = new Doctor("p1");
        Doctor doctor2 = new Doctor("p2");
        Doctor doctor3 = new Doctor("p3");

        restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        restTemplate.postForEntity("/doctor", doctor2, Doctor.class);
        restTemplate.postForEntity("/doctor", doctor3, Doctor.class);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/doctor", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("p1");
        assertThat(responseEntity.getBody()).contains("p2");
        assertThat(responseEntity.getBody()).contains("p3");
    }

}
