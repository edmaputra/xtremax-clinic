package com.xtremax.clinic.service;

import com.xtremax.clinic.domain.Medicine;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.MedicineRepository;
import com.xtremax.clinic.service.impl.MedicineService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MedicineServiceTest {

    @InjectMocks
    private MedicineService service;

    @Mock
    private MedicineRepository repository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addNew_shouldSuccess() throws Exception {
        Medicine p = new Medicine();
        p.setName("p");

        service.add(p);

        verify(repository, times(1)).save(p);
    }

    @Test
    public void whenFindByName_shouldReturnOneObject() throws Exception {
        when(repository.findByName("p1")).thenReturn(Optional.of(new Medicine("p1")));

        Medicine p1 = service.findByName("p1");

        verify(repository, times(1)).findByName("p1");
        assertThat(p1.getName()).isEqualTo("p1");
    }

    @Test
    public void whenFindAll_shouldReturnList() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList(new Medicine("p1"), new Medicine("p2"), new Medicine("p3")));

        List<Medicine> list = (List) service.retrieveAll();

        verify(repository, times(1)).findAll();
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(2).getName()).isEqualTo("p3");
    }

    @Test
    public void whenFindByName_shouldThrowNotFoundException() throws Exception {
        String nameToFind = "name";
        thrown.expect(ResourceNotFoundException.class);

        thrown.expectMessage(is("Entity with name "+ nameToFind +" not found"));

        Medicine medicine = service.findByName(nameToFind);

        assertThat(medicine).isNull();
    }

}