package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Doctor;
import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.domain.Treatment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TreatmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TreatmentRepository repository;

    private Patient p1;
    private Patient p2;
    private Patient p3;

    private Doctor d1;
    private Doctor d2;

    @Before
    public void setup() {
        p1 = new Patient();
        p1.setName("p1");
        entityManager.persist(p1);
        entityManager.flush();

        p2 = new Patient();
        p2.setName("p2");
        entityManager.persist(p2);
        entityManager.flush();

        p3 = new Patient();
        p3.setName("p3");
        entityManager.persist(p3);
        entityManager.flush();

        d1 = new Doctor();
        d1.setName("d1");
        entityManager.persist(d1);
        entityManager.flush();

        d2 = new Doctor();
        d2.setName("d2");
        entityManager.persist(d2);
        entityManager.flush();
    }

    @Test
    public void whenPatientRegisterForTreatment_registrationSuccessful() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue = new Treatment(p1, d1, date, time, false);
        entityManager.persist(queue);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue2 = new Treatment(p2, d1, date2, time2, false);
        entityManager.persist(queue2);
        entityManager.flush();

        List<Treatment> list = repository.findAll();

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getDoctor()).isEqualTo(d1);
        assertThat(list.get(1).getPatient()).isEqualTo(p2);
        assertThat(list.get(0).getRegistrationDate()).isEqualTo(date);
    }

    @Test
    public void whenCallFromPatientQueue_shouldReturnOldestRegistrationTimeAndNotYetCalled() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, false);
        entityManager.persist(queue1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:05");
        Treatment queue2 = new Treatment(p2, d1, date2, time2, false);
        entityManager.persist(queue2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue3 = new Treatment(p3, d1, date3, time3, false);
        entityManager.persist(queue3);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Optional<Treatment> treatment = repository.findTopByCalledFalseAndDoctorAndRegistrationDateOrderByRegistrationTimeAsc(d1, dateToFind);

        assertThat(treatment.get().getPatient()).isEqualTo(p1);
    }

    @Test
    public void whenPatient1IsTreatedAndCalled_shouldReturnPatient2() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, true);
        entityManager.persist(queue1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:05");
        Treatment queue2 = new Treatment(p2, d1, date2, time2, false);
        entityManager.persist(queue2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue3 = new Treatment(p3, d1, date3, time3, false);
        entityManager.persist(queue3);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Optional<Treatment> treatment = repository.findTopByCalledFalseAndDoctorAndRegistrationDateOrderByRegistrationTimeAsc(d1, dateToFind);

        assertThat(treatment.get().getPatient()).isEqualTo(p2);
    }

    @Test
    public void patient3ShouldCalled() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, true);
        entityManager.persist(queue1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:05");
        Treatment queue2 = new Treatment(p2, d1, date2, time2, true);
        entityManager.persist(queue2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue3 = new Treatment(p3, d1, date3, time3, false);
        entityManager.persist(queue3);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Optional<Treatment> treatment = repository.findTopByCalledFalseAndDoctorAndRegistrationDateOrderByRegistrationTimeAsc(d1, dateToFind);

        assertThat(treatment.get().getPatient()).isEqualTo(p3);
    }

    @Test
    public void countTreatment_shouldReturnCorrectSize() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, true);
        entityManager.persist(queue1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:05");
        Treatment queue2 = new Treatment(p2, d1, date2, time2, true);
        entityManager.persist(queue2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue3 = new Treatment(p3, d1, date3, time3, false);
        entityManager.persist(queue3);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Integer count = repository.countByDoctorAndRegistrationDate(d1, dateToFind);

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void registerTreatmentForDoctor2_shouldReturnCorrectSize() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, true);
        entityManager.persist(queue1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("10:05");
        Treatment queue2 = new Treatment(p2, d2, date2, time2, true);
        entityManager.persist(queue2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("10:10");
        Treatment queue3 = new Treatment(p3, d1, date3, time3, false);
        entityManager.persist(queue3);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Integer count = repository.countByDoctorAndRegistrationDate(d2, dateToFind);

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void whenCountForDoctor2_shouldReturnZero() throws Exception {
        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Integer count = repository.countByDoctorAndRegistrationDate(d2, dateToFind);

        assertThat(count).isEqualTo(0);
    }

    @Test
    public void whenFindTreatmentByPatientAndDate_shouldReturnCorrectTreatment() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, false);
        entityManager.persist(queue1);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Optional<List<Treatment>> treatment = repository.findByPatientAndRegistrationDate(p1, dateToFind);

        assertThat(treatment.get().get(0).getPatient()).isEqualTo(p1);
        assertThat(treatment.get().get(0)).isEqualTo(queue1);
    }

    @Test
    public void whenFindTreatmentByWrongDate_shouldReturnNull() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, false);
        entityManager.persist(queue1);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-26");
        Optional<List<Treatment>> treatment = repository.findByPatientAndRegistrationDate(p1, dateToFind);

        assertThat(treatment.isPresent()).isFalse();
    }

    @Test
    public void whenFindTreatmentByWrongPatient_shouldReturnNull() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("10:00");
        Treatment queue1 = new Treatment(p1, d1, date1, time1, false);
        entityManager.persist(queue1);
        entityManager.flush();

        Date dateToFind = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Optional<List<Treatment>> treatment = repository.findByPatientAndRegistrationDate(p2, dateToFind);

        assertThat(treatment.isPresent()).isFalse();
    }

}