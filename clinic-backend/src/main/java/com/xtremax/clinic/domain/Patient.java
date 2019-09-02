package com.xtremax.clinic.domain;

import io.github.edmaputra.edtmplte.domain.ABaseNamedEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Patient extends ABaseNamedEntity {
    public Patient() {
    }

    public Patient(@NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(name);
    }

    public Patient(Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(id, name);
    }

    public Patient(String version, Date createdOn, String creator, Date updatedOn, String updater, boolean recorded, Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(version, createdOn, creator, updatedOn, updater, recorded, id, name);
    }
}
