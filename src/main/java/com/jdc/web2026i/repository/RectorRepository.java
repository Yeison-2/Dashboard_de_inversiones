package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.RectorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RectorRepository extends CrudRepository<RectorEntity, Long> {

    List<RectorEntity> findByTipoIgnoreCase(String tipo);

    @Transactional
    @Query("SELECT r FROM RectorEntity r WHERE UPPER(TRIM(r.tipo)) = UPPER(TRIM(:tipo))")
    List<RectorEntity> findByDocumentoIdentidad(@Param("tipo") String tipo);
}
