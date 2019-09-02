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
public class Medicine extends ABaseNamedEntity {

    private Integer processDuration;

    public Medicine() {
    }

    public Medicine(@NotBlank(message = "Name is Blank. Please Fill the Detail") String name) {
        super(name);
    }

    public Medicine(Integer processDuration) {
        this.processDuration = processDuration;
    }

    public Medicine(@NotBlank(message = "Name is Blank. Please Fill the Detail") String name, Integer processDuration) {
        super(name);
        this.processDuration = processDuration;
    }

    public Medicine(Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name, Integer processDuration) {
        super(id, name);
        this.processDuration = processDuration;
    }

    public Medicine(String version, Date createdOn, String creator, Date updatedOn, String updater, boolean recorded, Long id, @NotBlank(message = "Name is Blank. Please Fill the Detail") String name, Integer processDuration) {
        super(version, createdOn, creator, updatedOn, updater, recorded, id, name);
        this.processDuration = processDuration;
    }
}
