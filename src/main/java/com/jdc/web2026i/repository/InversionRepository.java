package com.jdc.web2026i.repository;

import com.jdc.web2026i.entities.FuenteInversion;
import com.jdc.web2026i.entities.InversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InversionRepository extends JpaRepository<InversionEntity, Integer> {

    List<InversionEntity> findByProyecto_IdOrderByFechaInversionDesc(Integer proyectoId);

    @Query("""
            SELECT COALESCE(SUM(i.monto), 0)
            FROM InversionEntity i
            WHERE i.fuenteInversion = :fuente
              AND (:municipioId IS NULL OR i.proyecto.municipio.id = :municipioId)
              AND (:sectorialId IS NULL OR i.proyecto.sectorial.id = :sectorialId)
              AND (:proyectoId IS NULL OR i.proyecto.id = :proyectoId)
            """)
    BigDecimal totalPorFuente(@Param("fuente") FuenteInversion fuente,
                              @Param("municipioId") Integer municipioId,
                              @Param("sectorialId") Integer sectorialId,
                              @Param("proyectoId") Integer proyectoId);

    @Query("""
            SELECT DISTINCT i
            FROM InversionEntity i
            JOIN FETCH i.proyecto p
            JOIN FETCH p.municipio
            JOIN FETCH p.sectorial
            WHERE (:municipioId IS NULL OR p.municipio.id = :municipioId)
              AND (:sectorialId IS NULL OR p.sectorial.id = :sectorialId)
              AND (:proyectoId IS NULL OR p.id = :proyectoId)
            ORDER BY i.fechaInversion DESC
            """)
    List<InversionEntity> buscarConFiltros(@Param("municipioId") Integer municipioId,
                                           @Param("sectorialId") Integer sectorialId,
                                           @Param("proyectoId") Integer proyectoId);

    @Query("""
            SELECT DISTINCT i
            FROM InversionEntity i
            JOIN FETCH i.proyecto p
            JOIN FETCH p.municipio
            JOIN FETCH p.sectorial
            WHERE p.municipio.id = :municipioId
              AND i.fuenteInversion = :fuente
              AND (:sectorialId IS NULL OR p.sectorial.id = :sectorialId)
              AND i.fechaInversion BETWEEN :desde AND :hasta
            ORDER BY i.fechaInversion DESC
            """)
    List<InversionEntity> findByMunicipioFuenteYFechas(@Param("municipioId") Integer municipioId,
                                                        @Param("fuente") FuenteInversion fuente,
                                                        @Param("sectorialId") Integer sectorialId,
                                                        @Param("desde") LocalDate desde,
                                                        @Param("hasta") LocalDate hasta);
}
