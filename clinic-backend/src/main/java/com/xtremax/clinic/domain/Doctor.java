package com.xtremax.clinic.domain;

import io.github.edmaputra.edtmplte.domain.ABaseNamedEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Doctor extends ABaseNamedEntity {
    public Doctor() {
    }

    public Doctor(@NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(name);
    }

    public Doctor(Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(id, name);
    }

    public Doctor(String version, Date createdOn, String creator, Date updatedOn, String updater, boolean recorded, Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(version, createdOn, creator, updatedOn, updater, recorded, id, name);
    }
}
