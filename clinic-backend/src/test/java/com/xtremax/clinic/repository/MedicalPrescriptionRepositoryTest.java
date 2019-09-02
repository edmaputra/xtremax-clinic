package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MedicalPrescriptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicalPrescriptionRepository repository;

    private Patient p1;
    private Patient p2;
    private Patient p3;

    private Doctor d1;
    private Doctor d2;

    private Treatment t1;
    private Treatment t2;
    private Treatment t3;
    private Treatment t4;
    private Treatment t5;

    private Medicine m1;
    private Medicine m2;
    private Medicine m3;
    private Medicine m4;
    private Medicine m5;
    private Medicine m6;

    private MedicalPrescription mp1;
    private MedicalPrescription mp2;
    private MedicalPrescription mp3;
    private MedicalPrescription mp4;

    @Before
    public void setup() throws Exception {
        p1 = new Patient("p1");
        entityManager.persist(p1);
        entityManager.flush();

        p2 = new Patient("p2");
        entityManager.persist(p2);
        entityManager.flush();

        p3 = new Patient("p3");
        entityManager.persist(p3);
        entityManager.flush();

        d1 = new Doctor("d1");
        entityManager.persist(d1);
        entityManager.flush();

        d2 = new Doctor("d2");
        entityManager.persist(d2);
        entityManager.flush();

        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time1 = new SimpleDateFormat("HH:mm").parse("09:00");
        t1 = new Treatment(p1, d1, date1, time1, false);
        entityManager.persist(t1);
        entityManager.flush();

        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time2 = new SimpleDateFormat("HH:mm").parse("09:05");
        t2 = new Treatment(p2, d1, date2, time2, false);
        entityManager.persist(t2);
        entityManager.flush();

        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time3 = new SimpleDateFormat("HH:mm").parse("09:10");
        t3 = new Treatment(p3, d2, date3, time3, false);
        entityManager.persist(t3);
        entityManager.flush();

        Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time4 = new SimpleDateFormat("HH:mm").parse("09:15");
        t4 = new Treatment(p1, d2, date4, time4, false);
        entityManager.persist(t4);
        entityManager.flush();

        Date date5 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-27");
        Date time5 = new SimpleDateFormat("HH:mm").parse("09:30");
        t5 = new Treatment(p3, d2, date5, time5, false);
        entityManager.persist(t5);
        entityManager.flush();

        m1 = new Medicine("m1", 60);
        entityManager.persist(m1);
        entityManager.flush();

        m2 = new Medicine("m2", 120);
        entityManager.persist(m2);
        entityManager.flush();

        m3 = new Medicine("m3", 180);
        entityManager.persist(m3);
        entityManager.flush();

        m4 = new Medicine("m4", 240);
        entityManager.persist(m4);
        entityManager.flush();

        m5 = new Medicine("m5", 300);
        entityManager.persist(m5);
        entityManager.flush();

        m6 = new Medicine("m6", 360);
        entityManager.persist(m6);
        entityManager.flush();

        Date dateMp1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-27 10:00");
        mp1 = init(t1, Arrays.asList(m1, m2), dateMp1);
        entityManager.persist(mp1);
        entityManager.flush();

        Date dateMp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-27 10:30");
        mp2 = init(t2, Arrays.asList(m2, m3), dateMp2);
        entityManager.persist(mp2);
        entityManager.flush();

        Date dateMp3 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-27 11:00");
        mp3 = init(t3, Arrays.asList(m3, m4), dateMp3);
        entityManager.persist(mp3);
        entityManager.flush();

        Date dateMp4 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-27 11:30");
        mp4 = init(t4, Arrays.asList(m4, m5), dateMp4);
        entityManager.persist(mp4);
        entityManager.flush();
    }

    @Test
    public void whenFindMPByTimeBefore_shouldReturnEmpty() throws Exception {
        Date timeToFind = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-27 09:00");

        Optional<List<MedicalPrescription>> found = repository.findAllFinished(timeToFind);

        assertThat(found.isPresent()).isFalse();
    }

    private MedicalPrescription init(Treatment t, List<Medicine> medicines, Date time) {
        List<MedicalPrescriptionDetail> list = new ArrayList<>();
        for (Medicine m: medicines){
            MedicalPrescriptionDetail mpd = new MedicalPrescriptionDetail();
            mpd.setMedicine(m);
            mpd.setAmount(1);
            list.add(mpd);
        }
        MedicalPrescription mp = new MedicalPrescription();
        mp.setTreatment(t);
        mp.setDetails(list);
        mp.setRegisteredAt(time);
        mp.setDoneAt(time);
        return mp;
    }



}