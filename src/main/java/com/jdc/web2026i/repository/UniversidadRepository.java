package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.UniversidadEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UniversidadRepository extends CrudRepository<UniversidadEntity, Long> {
    @Transactional
    @Query("SELECT col FROM UniversidadEntity col WHERE col.idUniversidad NOT IN"+
            "(SELECT dir.universidad.idUniversidad FROM RectorEntity dir)" +
            "AND col.estado=true")
    public List<UniversidadEntity> findByRector();

    // implementar findByNombreComienza

    @Query("SELECT u FROM UniversidadEntity u WHERE UPPER(u.nombre) LIKE UPPER(CONCAT(:nombreComienzaU, '%'))")
    List<UniversidadEntity> findByNombreComienza(@Param("nombreComienzaU") String nombreComienzaU);





}
