package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.domain.Treatment;
import com.xtremax.clinic.domain.handler.TreatmentHandler;
import com.xtremax.clinic.exception.QueueLimitReachedException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TreatmentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Patient patient1 = new Patient("p1");
    private Patient patient2 = new Patient("p2");
    private Patient patient3 = new Patient("p3");

    private Doctor doctor1 = new Doctor("d1");
    private Doctor doctor2 = new Doctor("d2");

    @Test
    public void whenPatientRegisterToDoctorTreatment_thenThrowException() throws ResourceNotFoundException {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();
        Long patientId = 1l;

        TreatmentHandler handler = new TreatmentHandler(doctorId, patientId, new Date());

        restTemplate.postForEntity("/queue/doctor/push", handler, String.class);
    }

    @Test
    public void whenPatientRegisterToDoctorTreatment_thenShouldReturnTreatment() {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId = responseForSavingPatient.getBody().getId();

        Date timeRegistration = new Date();

        TreatmentHandler handler = new TreatmentHandler(doctorId, patientId, timeRegistration);

        ResponseEntity<Treatment> responseEntity = restTemplate.postForEntity("/queue/doctor/push", handler, Treatment.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getPatient().getName()).isEqualTo(patient1.getName());
        assertThat(responseEntity.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());
        assertThat(responseEntity.getBody().getRegistrationDate()).isEqualTo(timeRegistration);
        assertThat(responseEntity.getBody().getRegistrationTime()).isEqualTo(timeRegistration);
        assertThat(responseEntity.getBody().isCalled()).isFalse();
    }

    @Test
    public void whenPatientRegisterToDoctorTreatmentAndListSizeIsFifteen_thenThrowException() throws ParseException, QueueLimitReachedException {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor2, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId = responseForSavingPatient.getBody().getId();

        for (int i = 0; i < 15; i++) {
            int temp = 11 + i;
            String dateString = "2019-07-30 09:" + temp;
            Date timeRegistration = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
            TreatmentHandler handler = new TreatmentHandler(doctorId, patientId, timeRegistration);
            restTemplate.postForEntity("/queue/doctor/push", handler, Treatment.class);
        }

        ResponseEntity<Patient> responseForSavingPatient2 = restTemplate.postForEntity("/patient", patient2, Patient.class);
        Long patientId2 = responseForSavingPatient2.getBody().getId();
        Date timeRegistration2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:30");
        TreatmentHandler handler = new TreatmentHandler(doctorId, patientId2, timeRegistration2);
        restTemplate.postForEntity("/queue/doctor/push", handler, Treatment.class);
    }

    @Test
    public void whenQueueCallAndQueueIsEmpty_shouldThrowException() throws ResourceNotFoundException {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        String url = "/queue/doctor/"+doctorId+"/call";

        ResponseEntity<String> rFinal = restTemplate.getForEntity(url, String.class);
    }

    @Test
    public void whenQueueList_thenShouldReturnList() throws ParseException {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient1 = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId1 = responseForSavingPatient1.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient2 = restTemplate.postForEntity("/patient", patient2, Patient.class);
        Long patientId2 = responseForSavingPatient2.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient3 = restTemplate.postForEntity("/patient", patient3, Patient.class);
        Long patientId3 = responseForSavingPatient3.getBody().getId();

        Date timeRegistration1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:00");
        Date timeRegistration2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:05");
        Date timeRegistration3 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:10");

        TreatmentHandler handler1 = new TreatmentHandler(doctorId, patientId1, timeRegistration1);
        TreatmentHandler handler2 = new TreatmentHandler(doctorId, patientId2, timeRegistration2);
        TreatmentHandler handler3 = new TreatmentHandler(doctorId, patientId3, timeRegistration3);

        ResponseEntity<Treatment> r1 = restTemplate.postForEntity("/queue/doctor/push", handler1, Treatment.class);
        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r1.getBody().getPatient().getName()).isEqualTo(patient1.getName());
        assertThat(r1.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        ResponseEntity<Treatment> r2 = restTemplate.postForEntity("/queue/doctor/push", handler2, Treatment.class);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r2.getBody().getPatient().getName()).isEqualTo(patient2.getName());
        assertThat(r2.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        ResponseEntity<Treatment> r3 =restTemplate.postForEntity("/queue/doctor/push", handler3, Treatment.class);
        assertThat(r3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r3.getBody().getPatient().getName()).isEqualTo(patient3.getName());
        assertThat(r3.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        assertThat(r3.getBody().getDoctor().getId()).isEqualTo(doctorId);

        String url = "/queue/doctor/"+doctorId+"/list";

        ResponseEntity<List> rFinal = restTemplate.getForEntity(url, List.class);
        assertThat(rFinal.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rFinal.getBody().size()).isEqualTo(3);

    }

    @Test
    public void whenQueueCall_thenShouldReturnPatientWithCorrectOrder() throws ParseException {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient1 = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId1 = responseForSavingPatient1.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient2 = restTemplate.postForEntity("/patient", patient2, Patient.class);
        Long patientId2 = responseForSavingPatient2.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient3 = restTemplate.postForEntity("/patient", patient3, Patient.class);
        Long patientId3 = responseForSavingPatient3.getBody().getId();

        Date timeRegistration1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:00");
        Date timeRegistration2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:05");
        Date timeRegistration3 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:10");

        TreatmentHandler handler1 = new TreatmentHandler(doctorId, patientId1, timeRegistration1);
        TreatmentHandler handler2 = new TreatmentHandler(doctorId, patientId2, timeRegistration2);
        TreatmentHandler handler3 = new TreatmentHandler(doctorId, patientId3, timeRegistration3);

        ResponseEntity<Treatment> r1 = restTemplate.postForEntity("/queue/doctor/push", handler1, Treatment.class);
        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r1.getBody().getPatient().getName()).isEqualTo(patient1.getName());
        assertThat(r1.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        ResponseEntity<Treatment> r2 = restTemplate.postForEntity("/queue/doctor/push", handler2, Treatment.class);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r2.getBody().getPatient().getName()).isEqualTo(patient2.getName());
        assertThat(r2.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        ResponseEntity<Treatment> r3 =restTemplate.postForEntity("/queue/doctor/push", handler3, Treatment.class);
        assertThat(r3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r3.getBody().getPatient().getName()).isEqualTo(patient3.getName());
        assertThat(r3.getBody().getDoctor().getName()).isEqualTo(doctor1.getName());

        assertThat(r3.getBody().getDoctor().getId()).isEqualTo(doctorId);

        String url = "/queue/doctor/"+doctorId+"/call?date=2019-07-30";

        ResponseEntity<Patient> rFinal = restTemplate.getForEntity(url, Patient.class);
        assertThat(rFinal.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rFinal.getBody().getName()).isEqualTo(patient1.getName());

        ResponseEntity<Patient> rFinal2 = restTemplate.getForEntity(url, Patient.class);
        assertThat(rFinal2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rFinal2.getBody().getName()).isEqualTo(patient2.getName());

        ResponseEntity<Patient> rFinal3 = restTemplate.getForEntity(url, Patient.class);
        assertThat(rFinal3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rFinal3.getBody().getName()).isEqualTo(patient3.getName());

    }

    private void saveEntity(Doctor doctor, Patient patient, Date date) {

    }
}
