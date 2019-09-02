package com.xtremax.clinic.service;

import com.xtremax.clinic.domain.Patient;
import com.xtremax.clinic.exception.ResourceNotFoundException;
import com.xtremax.clinic.repository.PatientRepository;
import com.xtremax.clinic.service.impl.PatientService;
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
public class PatientServiceTest {

    @InjectMocks
    private PatientService service;

    @Mock
    private PatientRepository repository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addNew_shouldSuccess() throws Exception{
        Patient p = new Patient();
        p.setName("p");

        service.add(p);

        verify(repository, times(1)).save(p);
    }

    @Test
    public void whenFindByName_shouldReturnOneObject() throws Exception {
        when(repository.findByName("p1")).thenReturn(Optional.of(new Patient("p1")));

        Patient p1 = service.findByName("p1");

        verify(repository, times(1)).findByName("p1");
        assertThat(p1.getName()).isEqualTo("p1");
    }

    @Test
    public void whenFindAll_shouldReturnList() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList(new Patient("p1"), new Patient("p2"), new Patient("p3")));

        List<Patient> list = (List) service.retrieveAll();

        verify(repository, times(1)).findAll();
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(2).getName()).isEqualTo("p3");
    }

    @Test
    public void whenFindByName_shouldThrowNotFoundException() throws Exception {
        String nameToFind = "name";
        thrown.expect(ResourceNotFoundException.class);

        thrown.expectMessage(is("Entity with name "+ nameToFind +" not found"));

        Patient patient = service.findByName(nameToFind);

        assertThat(patient).isNull();
    }

}