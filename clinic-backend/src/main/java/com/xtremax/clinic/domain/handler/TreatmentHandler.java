package com.xtremax.clinic.domain.handler;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TreatmentHandler {

    Long doctorId;

    Long patientId;

    Date registrationTime;

}
