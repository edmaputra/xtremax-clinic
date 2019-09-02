package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Patient;
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
public class PatientIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String name = "bangun";

    @Test
    public void whenSaveNewEntity_shouldReturnCorrectEntity() {
        Patient patient = new Patient(name);
        ResponseEntity<Patient> responseEntity = restTemplate.postForEntity("/patient", patient, Patient.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldReturnCorrectEntity() {
        Patient patient = new Patient(name);
        restTemplate.postForEntity("/patient", patient, Patient.class);

        ResponseEntity<Patient> responseEntity = restTemplate.getForEntity("/patient/name/"+name, Patient.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldThrowException() throws ResourceNotFoundException {
        restTemplate.getForEntity("/patient/name/"+name, String.class);
    }

    @Test
    public void whenFindAll_shouldReturnListEntity() {
        Patient patient1 = new Patient("p1");
        Patient patient2 = new Patient("p2");
        Patient patient3 = new Patient("p3");

        restTemplate.postForEntity("/patient", patient1, Patient.class);
        restTemplate.postForEntity("/patient", patient2, Patient.class);
        restTemplate.postForEntity("/patient", patient3, Patient.class);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/patient", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("p1");
        assertThat(responseEntity.getBody()).contains("p2");
        assertThat(responseEntity.getBody()).contains("p3");
    }

}
