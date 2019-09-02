package com.xtremax.clinic.domain;

import io.github.edmaputra.edtmplte.domain.ABaseIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Treatment extends ABaseIdEntity {

    @OneToOne
    private Patient patient;

    @OneToOne
    private Doctor doctor;

    @OneToOne
    private MedicalPrescription medicalPrescription;

    @Temporal(TemporalType.DATE)
    private Date registrationDate = new Date();

//    @Temporal(TemporalType.TIME)
    private Date registrationTime = new Date();

    private boolean called = false;

    private Integer number;

    @Transient
    private boolean resep = false;

    public Treatment(Patient patient, Doctor doctor, Date registrationDate, Date registrationTime, boolean called) {
        this.patient = patient;
        this.doctor = doctor;
        this.registrationDate = registrationDate;
        this.registrationTime = registrationTime;
        this.called = called;
    }
}
