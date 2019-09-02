package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Patient;
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
public class PatientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientRepository repository;

    @Test
    public void whenFindAll_ShouldReturnList() {
        Patient p1 = new Patient("p1");
        entityManager.persist(p1);
        entityManager.flush();

        Patient p2 = new Patient("p2");
        entityManager.persist(p2);
        entityManager.flush();

        List<Patient> patients = repository.findAll();

        assertThat(patients.size()).isEqualTo(2);
        assertThat(patients.get(0)).isEqualTo(p1);
    }

    @Test
    public void whenFindByName_ShouldReturnCorrectObject() {
        Patient p3 = new Patient();
        p3.setName("p3");
        entityManager.persist(p3);
        entityManager.flush();

        Optional<Patient> patient = repository.findByName("p3");

        assertThat(patient.get().getName()).isEqualTo(p3.getName());
    }

    @Test
    public void whenTryToSave_ResultShouldNotNull() {
        Patient p3 = new Patient("p3");
        Patient patient = repository.save(p3);

        assertThat(patient).isNotNull();
    }

}