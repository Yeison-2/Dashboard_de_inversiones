package com.jdc.web2026i.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de proyecto para API (detalle modal / relaciones proyecto–inversiones).
 */
public record ProyectoDetalleRespuestaDTO(
        Integer id,
        String nombreProyecto,
        LocalDate fechaRegistro,
        String estado,
        String tipo,
        String responsable,
        String municipioNombre,
        String municipioDepartamento,
        String sectorialNombre,
        BigDecimal inversionTotal,
        List<InversionApiDTO> inversiones
) {
    public record InversionApiDTO(
            Integer id,
            LocalDate fechaInversion,
            String fuenteInversion,
            BigDecimal monto,
            boolean fechaValidaVsProyecto
    ) {}
}
