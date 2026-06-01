package com.jdc.web2026i.DTO;

import java.math.BigDecimal;

public record DashboardResumenDTO(
        BigDecimal inversionMunicipio,
        BigDecimal inversionDepartamento,
        BigDecimal inversionNacion,
        BigDecimal inversionOtroAportante,
        BigDecimal inversionTotal
) {
}
