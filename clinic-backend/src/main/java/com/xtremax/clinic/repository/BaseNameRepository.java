package com.xtremax.clinic.repository;

import io.github.edmaputra.edtmplte.domain.ABaseEntity;
import io.github.edmaputra.edtmplte.repository.ABaseRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseNameRepository<T extends ABaseEntity, ID> extends ABaseRepository<T, ID> {

    Optional<T> findByName(String name);

}
