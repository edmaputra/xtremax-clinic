package com.xtremax.clinic.domain.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MedicalPrescriptionHandler {

    private Long treatmentId;

    private List<Detail> details;

    private Date time;

    @Getter
    @Setter
    public static class Detail {
        private Long medicineId;
        private Integer amount;
    }

}
