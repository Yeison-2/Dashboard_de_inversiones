package com.jdc.web2026i.services;

import com.jdc.web2026i.DTO.DashboardResumenDTO;
import com.jdc.web2026i.DTO.NombreValorDTO;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.ProyectoEntity;

import java.util.List;

public interface DashboardService {
    DashboardResumenDTO obtenerResumen(Integer municipioId, Integer sectorialId, Integer proyectoId);

    List<ProyectoEntity> filtrarProyectos(Integer municipioId, Integer sectorialId, Integer proyectoId, String estado);

    List<InversionEntity> filtrarInversiones(Integer municipioId, Integer sectorialId, Integer proyectoId);

    List<NombreValorDTO> agruparInversionPorMunicipio(List<InversionEntity> inversiones);
}
