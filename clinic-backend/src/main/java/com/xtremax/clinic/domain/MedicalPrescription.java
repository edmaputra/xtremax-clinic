package com.xtremax.clinic.domain;

import io.github.edmaputra.edtmplte.domain.ABaseIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicalPrescription extends ABaseIdEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date doneAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt;

    @OneToOne
    private Treatment treatment;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    private List<MedicalPrescriptionDetail> details;

}
