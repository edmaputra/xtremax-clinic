package com.xtremax.clinic.repository;

import com.xtremax.clinic.domain.Medicine;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends BaseNameRepository<Medicine, Long> {

}
