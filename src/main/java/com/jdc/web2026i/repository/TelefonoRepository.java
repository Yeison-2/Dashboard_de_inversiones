package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.TelefonoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TelefonoRepository extends CrudRepository<TelefonoEntity, Long> {

    @Query(value = "SELECT * FROM telefonos ORDER BY id_telefono ASC LIMIT 8", nativeQuery = true)
    List<TelefonoEntity> findPrimeros8();
}

