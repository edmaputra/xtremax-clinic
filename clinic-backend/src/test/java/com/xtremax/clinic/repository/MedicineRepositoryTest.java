package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Medicine;
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
public class MedicineRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicineRepository repository;

    @Test
    public void whenFindAll_ShouldReturnList() {
        Medicine p1 = new Medicine("m1");
        entityManager.persist(p1);
        entityManager.flush();

        Medicine p2 = new Medicine("m2");
        entityManager.persist(p2);
        entityManager.flush();

        List<Medicine> medicines = repository.findAll();

        assertThat(medicines.size()).isEqualTo(2);
        assertThat(medicines.get(0)).isEqualTo(p1);
    }

    @Test
    public void whenFindByName_ShouldReturnCorrectObject() {
        Medicine p3 = new Medicine("m3");
        entityManager.persist(p3);
        entityManager.flush();

        Optional<Medicine> medicine = repository.findByName("m3");

        assertThat(medicine.get().getName()).isEqualTo(p3.getName());
    }

    @Test
    public void whenTryToSave_ResultShouldNotNull() {
        Medicine m3 = new Medicine("m3");
        Medicine medicine = repository.save(m3);

        assertThat(medicine).isNotNull();
    }

}