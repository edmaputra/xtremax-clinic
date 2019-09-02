package com.xtremax.clinic.controller;

import com.xtremax.clinic.domain.*;
import com.xtremax.clinic.domain.handler.MedicalPrescriptionHandler;
import com.xtremax.clinic.domain.handler.TreatmentHandler;
import org.junit.Before;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MedicalPrescriptionIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Patient patient1 = new Patient("p1");
    private Patient patient2 = new Patient("p2");
    private Patient patient3 = new Patient("p3");

    private Doctor doctor1 = new Doctor("d1");
    private Doctor doctor2 = new Doctor("d2");

    private Medicine medicine1 = new Medicine("m1", 60);
    private Medicine medicine2 = new Medicine("m2", 120);
    private Medicine medicine3 = new Medicine("m3", 180);
    private Medicine medicine4 = new Medicine("m4", 240);
    private Medicine medicine5 = new Medicine("m5", 300);
    private Medicine medicine6 = new Medicine("m6", 360);

    private Date timeRegistration1;
    private Date timeRegistration2;
    private Date timeRegistration3;
    private Date timeRegistration4;
    private Date timeRegistration5;
    private Date timeRegistration6;

    private Treatment t1;
    private Treatment t2;
    private Treatment t3;

    @Before
    public void init() throws ParseException {
        timeRegistration1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:00");
        timeRegistration2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:05");
        timeRegistration3 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 09:10");
        timeRegistration4 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 10:00");
        timeRegistration5 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 10:05");
        timeRegistration6 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 10:10");

        t1 = new Treatment(patient1, doctor1, timeRegistration1, timeRegistration1, true);
        t2 = new Treatment(patient2, doctor1, timeRegistration2, timeRegistration2, true);
        t3 = new Treatment(patient3, doctor1, timeRegistration3, timeRegistration3, true);
    }

    @Test
    public void whenAddNewPrescription_shouldReturnMedicalPrescription() throws ParseException{
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId = responseForSavingPatient.getBody().getId();

        TreatmentHandler handler = new TreatmentHandler(doctorId, patientId, timeRegistration1);
        ResponseEntity<Treatment> responseEntity = restTemplate.postForEntity("/queue/doctor/push", handler, Treatment.class);
        Long treatmentId = responseEntity.getBody().getId();

        restTemplate.postForEntity("/medicine", medicine1, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine2, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine3, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine4, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine5, Medicine.class);

        Date timeMedicalPrescription = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 10:00");

        MedicalPrescriptionHandler h1 = initHandler(timeMedicalPrescription, Arrays.asList(medicine1, medicine4, medicine5), treatmentId);

        ResponseEntity<MedicalPrescription> rFinal = restTemplate.postForEntity("/queue/prescription/push", h1, MedicalPrescription.class);

        Date timeDoneShouldBeDone = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-30 10:10");

        assertThat(rFinal.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rFinal.getBody().getDoneAt()).isEqualTo(timeDoneShouldBeDone);

    }

    @Test
    public void whenFindPrescriptionByDoneTime_shouldReturnCorrectOrder() {
        ResponseEntity<Doctor> responseForSavingDoctor = restTemplate.postForEntity("/doctor", doctor1, Doctor.class);
        Long doctorId = responseForSavingDoctor.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient1 = restTemplate.postForEntity("/patient", patient1, Patient.class);
        Long patientId1 = responseForSavingPatient1.getBody().getId();

        TreatmentHandler handler1 = new TreatmentHandler(doctorId, patientId1, timeRegistration1);
        ResponseEntity<Treatment> responseEntity1 = restTemplate.postForEntity("/queue/doctor/push", handler1, Treatment.class);
        Long treatmentId1 = responseEntity1.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient2 = restTemplate.postForEntity("/patient", patient2, Patient.class);
        Long patientId2 = responseForSavingPatient2.getBody().getId();

        TreatmentHandler handler2 = new TreatmentHandler(doctorId, patientId2, timeRegistration2);
        ResponseEntity<Treatment> responseEntity2 = restTemplate.postForEntity("/queue/doctor/push", handler2, Treatment.class);
        Long treatmentId2 = responseEntity2.getBody().getId();

        ResponseEntity<Patient> responseForSavingPatient3 = restTemplate.postForEntity("/patient", patient3, Patient.class);
        Long patientId3 = responseForSavingPatient3.getBody().getId();

        TreatmentHandler handler3 = new TreatmentHandler(doctorId, patientId3, timeRegistration3);
        ResponseEntity<Treatment> responseEntity3 = restTemplate.postForEntity("/queue/doctor/push", handler3, Treatment.class);
        Long treatmentId3 = responseEntity3.getBody().getId();

        restTemplate.postForEntity("/medicine", medicine1, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine2, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine3, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine4, Medicine.class);
        restTemplate.postForEntity("/medicine", medicine5, Medicine.class);

        MedicalPrescriptionHandler h1 = initHandler(timeRegistration4, Arrays.asList(medicine2, medicine4, medicine3, medicine5), treatmentId1);

        ResponseEntity<MedicalPrescription> r1 = restTemplate.postForEntity("/queue/prescription/push", h1, MedicalPrescription.class);

        MedicalPrescriptionHandler h2 = initHandler(timeRegistration5, Arrays.asList(medicine1), treatmentId2);

        ResponseEntity<MedicalPrescription> r2 = restTemplate.postForEntity("/queue/prescription/push", h2, MedicalPrescription.class);

        MedicalPrescriptionHandler h3 = initHandler(timeRegistration6, Arrays.asList(medicine1, medicine2), treatmentId3);

        ResponseEntity<MedicalPrescription> r3 = restTemplate.postForEntity("/queue/prescription/push", h3, MedicalPrescription.class);

        String url1 = "/queue/prescription/done?date=2019-07-30_10:30";

        ResponseEntity<String> rFinal1 = restTemplate.getForEntity(url1, String.class);
        assertThat(rFinal1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private MedicalPrescriptionHandler initHandler(Date time, List<Medicine> medicines, Long treatmentId){
        MedicalPrescriptionHandler h = new MedicalPrescriptionHandler();
        h.setTime(time);
        h.setTreatmentId(treatmentId);
        List<MedicalPrescriptionHandler.Detail> details = new ArrayList<>();
        for (Medicine m: medicines) {
            MedicalPrescriptionHandler.Detail detail = new MedicalPrescriptionHandler.Detail();
            detail.setMedicineId(m.getId());
            detail.setAmount(1);
            details.add(detail);
        }
        h.setDetails(details);
        return h;
    }
}
