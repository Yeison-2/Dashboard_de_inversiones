package com.jdc.web2026i.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InversionesMultiplesDTO {

    private Integer proyectoId;
    private LocalDate fechaInversion;
    private BigDecimal montoMunicipio;
    private BigDecimal montoDepartamento;
    private BigDecimal montoNacion;
    private BigDecimal montoOtroAportante;
    private String nombreOtroAportante;

    public InversionesMultiplesDTO() {
    }

    public InversionesMultiplesDTO(Integer proyectoId, LocalDate fechaInversion,
                                   BigDecimal montoMunicipio, BigDecimal montoDepartamento,
                                   BigDecimal montoNacion, BigDecimal montoOtroAportante,
                                   String nombreOtroAportante) {
        this.proyectoId = proyectoId;
        this.fechaInversion = fechaInversion;
        this.montoMunicipio = montoMunicipio;
        this.montoDepartamento = montoDepartamento;
        this.montoNacion = montoNacion;
        this.montoOtroAportante = montoOtroAportante;
        this.nombreOtroAportante = nombreOtroAportante;
    }

    // Getters y Setters
    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    public LocalDate getFechaInversion() {
        return fechaInversion;
    }

    public void setFechaInversion(LocalDate fechaInversion) {
        this.fechaInversion = fechaInversion;
    }

    public BigDecimal getMontoMunicipio() {
        return montoMunicipio;
    }

    public void setMontoMunicipio(BigDecimal montoMunicipio) {
        this.montoMunicipio = montoMunicipio;
    }

    public BigDecimal getMontoDepartamento() {
        return montoDepartamento;
    }

    public void setMontoDepartamento(BigDecimal montoDepartamento) {
        this.montoDepartamento = montoDepartamento;
    }

    public BigDecimal getMontoNacion() {
        return montoNacion;
    }

    public void setMontoNacion(BigDecimal montoNacion) {
        this.montoNacion = montoNacion;
    }

    public BigDecimal getMontoOtroAportante() {
        return montoOtroAportante;
    }

    public void setMontoOtroAportante(BigDecimal montoOtroAportante) {
        this.montoOtroAportante = montoOtroAportante;
    }

    public String getNombreOtroAportante() {
        return nombreOtroAportante;
    }

    public void setNombreOtroAportante(String nombreOtroAportante) {
        this.nombreOtroAportante = nombreOtroAportante;
    }
}

