package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.EstadoProyecto;
import com.jdc.web2026i.entities.ProyectoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<ProyectoEntity, Integer>, JpaSpecificationExecutor<ProyectoEntity> {

    List<ProyectoEntity> findByEstadoAndFechaRegistroBetween(EstadoProyecto estado, LocalDate fechaInicio, LocalDate fechaFin);

    List<ProyectoEntity> findBySectorial_Id(Integer sectorialId);

    List<ProyectoEntity> findByEstadoAndFechaRegistroBetweenAndSectorial_Id(EstadoProyecto estado, LocalDate fechaInicio, LocalDate fechaFin, Integer sectorialId);

    @EntityGraph(attributePaths = {"sectorial", "municipio", "inversiones"})
    @Query("SELECT DISTINCT p FROM ProyectoEntity p WHERE p.id = :id")
    Optional<ProyectoEntity> findDetailedById(@Param("id") Integer id);
}
