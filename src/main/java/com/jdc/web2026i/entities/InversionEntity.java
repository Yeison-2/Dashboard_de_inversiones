package com.jdc.web2026i.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inversiones")
public class InversionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinversiones")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproyecto", nullable = false)
    private ProyectoEntity proyecto;

    @Column(name = "fecha_inversion", nullable = false)
    private LocalDate fechaInversion;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuente_inversion", nullable = false, length = 20)
    private FuenteInversion fuenteInversion;

    @Column(name = "monto", nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(name = "nombre_aportante", length = 100)
    private String nombreAportante;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProyectoEntity getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoEntity proyecto) {
        this.proyecto = proyecto;
    }

    public LocalDate getFechaInversion() {
        return fechaInversion;
    }

    public void setFechaInversion(LocalDate fechaInversion) {
        this.fechaInversion = fechaInversion;
    }

    public FuenteInversion getFuenteInversion() {
        return fuenteInversion;
    }

    public void setFuenteInversion(FuenteInversion fuenteInversion) {
        this.fuenteInversion = fuenteInversion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getNombreAportante() {
        return nombreAportante;
    }

    public void setNombreAportante(String nombreAportante) {
        this.nombreAportante = nombreAportante;
    }
}
