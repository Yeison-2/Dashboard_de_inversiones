package com.jdc.web2026i.services;

import com.jdc.web2026i.DTO.DashboardResumenDTO;
import com.jdc.web2026i.entities.EstadoProyecto;
import com.jdc.web2026i.entities.FuenteInversion;
import com.jdc.web2026i.entities.InversionEntity;
import com.jdc.web2026i.entities.ProyectoEntity;
import com.jdc.web2026i.repository.InversionRepository;
import com.jdc.web2026i.repository.ProyectoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.jdc.web2026i.DTO.NombreValorDTO;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InversionRepository inversionRepository;
    private final ProyectoRepository proyectoRepository;

    public DashboardServiceImpl(InversionRepository inversionRepository, ProyectoRepository proyectoRepository) {
        this.inversionRepository = inversionRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public DashboardResumenDTO obtenerResumen(Integer municipioId, Integer sectorialId, Integer proyectoId) {
        BigDecimal municipio = inversionRepository.totalPorFuente(FuenteInversion.MUNICIPIO, municipioId, sectorialId, proyectoId);
        BigDecimal departamento = inversionRepository.totalPorFuente(FuenteInversion.DEPARTAMENTO, municipioId, sectorialId, proyectoId);
        BigDecimal nacion = inversionRepository.totalPorFuente(FuenteInversion.NACION, municipioId, sectorialId, proyectoId);
        BigDecimal otro = inversionRepository.totalPorFuente(FuenteInversion.OTRO_APORTANTE, municipioId, sectorialId, proyectoId);
        BigDecimal total = municipio.add(departamento).add(nacion).add(otro);
        return new DashboardResumenDTO(municipio, departamento, nacion, otro, total);
    }

    @Override
    public List<ProyectoEntity> filtrarProyectos(Integer municipioId, Integer sectorialId, Integer proyectoId, String estado) {
        Specification<ProyectoEntity> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (municipioId != null) {
                predicates.add(cb.equal(root.get("municipio").get("id"), municipioId));
            }
            if (sectorialId != null) {
                predicates.add(cb.equal(root.get("sectorial").get("id"), sectorialId));
            }
            if (proyectoId != null) {
                predicates.add(cb.equal(root.get("id"), proyectoId));
            }
            if (estado != null && !estado.isBlank()) {
                predicates.add(cb.equal(root.get("estado"), EstadoProyecto.valueOf(estado.toUpperCase(Locale.ROOT))));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return proyectoRepository.findAll(specification);
    }

    @Override
    public List<InversionEntity> filtrarInversiones(Integer municipioId, Integer sectorialId, Integer proyectoId) {
        return inversionRepository.buscarConFiltros(municipioId, sectorialId, proyectoId);
    }

    @Override
    public List<NombreValorDTO> agruparInversionPorMunicipio(List<InversionEntity> inversiones) {
        Map<String, BigDecimal> acumulado = new LinkedHashMap<>();
        for (InversionEntity i : inversiones) {
            String nombre = i.getProyecto().getMunicipio().getNombre();
            acumulado.merge(nombre, i.getMonto(), BigDecimal::add);
        }
        return acumulado.entrySet().stream()
                .map(e -> new NombreValorDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(NombreValorDTO::valor).reversed())
                .collect(Collectors.toList());
    }
}
