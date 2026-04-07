package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.SeleccionalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeleccionalRepository extends CrudRepository<SeleccionalEntity, Long> {

    List<SeleccionalEntity> findByIdSeleccionalGreaterThanEqual(Integer idMinimo);

}
