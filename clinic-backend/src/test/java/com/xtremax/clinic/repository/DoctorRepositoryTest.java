package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Doctor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DoctorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DoctorRepository repository;

    @Test
    public void whenFindAll_ShouldReturnList() {
        Doctor p1 = new Doctor("p1");
        entityManager.persist(p1);
        entityManager.flush();

        Doctor p2 = new Doctor("p2");
        entityManager.persist(p2);
        entityManager.flush();

        List<Doctor> doctors = repository.findAll();

        assertThat(doctors.size()).isEqualTo(2);
        assertThat(doctors.get(0)).isEqualTo(p1);
    }

    @Test
    public void whenFindByName_ShouldReturnCorrectObject() {
        Doctor p3 = new Doctor();
        p3.setName("p3");
        entityManager.persist(p3);
        entityManager.flush();

        Optional<Doctor> doctor = repository.findByName("p3");

        assertThat(doctor.get().getName()).isEqualTo(p3.getName());
    }

    @Test
    public void whenTryToSave_ResultShouldNotNull() {
        Doctor p3 = new Doctor();
        p3.setName("p3");
        Doctor doctor = (Doctor) repository.save(p3);

        assertThat(doctor).isNotNull();
    }

}