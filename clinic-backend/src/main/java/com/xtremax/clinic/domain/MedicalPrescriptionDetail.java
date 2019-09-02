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
public class MedicalPrescriptionDetail extends ABaseIdEntity {

    @ManyToOne
    private Medicine medicine;

    private Integer amount;

}
