package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Medicine;
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
public class MedicineIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String name = "bangun";

    @Test
    public void whenSaveNewEntity_shouldReturnCorrectEntity() {
        Medicine medicine = new Medicine(name);
        ResponseEntity<Medicine> responseEntity = restTemplate.postForEntity("/medicine", medicine, Medicine.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldReturnCorrectEntity() {
        Medicine medicine = new Medicine(name, 70);
        restTemplate.postForEntity("/medicine", medicine, Medicine.class);

        ResponseEntity<Medicine> responseEntity = restTemplate.getForEntity("/medicine/name/"+name, Medicine.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo(name);
    }

    @Test
    public void whenFindByName_shouldThrowException() throws ResourceNotFoundException {
        restTemplate.getForEntity("/medicine/name/"+name, String.class);
    }

    @Test
    public void whenFindAll_shouldReturnListEntity() {
        Medicine medicine1 = new Medicine("p1");
        Medicine medicine2 = new Medicine("p2");
        Medicine medicine3 = new Medicine("p3");

        restTemplate.postForEntity("/medicine", medicine1, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine2, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine3, Medicine.class);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/medicine", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("p1");
        assertThat(responseEntity.getBody()).contains("p2");
        assertThat(responseEntity.getBody()).contains("p3");
    }

}
