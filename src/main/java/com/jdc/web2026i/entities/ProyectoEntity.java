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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyectos")
public class ProyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproyectos")
    private Integer id;

    @Column(name = "nombre_proyecto", nullable = false, length = 80)
    private String nombreProyecto;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoProyecto estado;

    @Column(name = "tipo", nullable = false, length = 80)
    private String tipo;

    @Column(name = "responsable", nullable = false, length = 80)
    private String responsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsectorial", nullable = false)
    private SectorialEntity sectorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipio", nullable = false)
    private MunicipioEntity municipio;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    private List<InversionEntity> inversiones = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public SectorialEntity getSectorial() {
        return sectorial;
    }

    public void setSectorial(SectorialEntity sectorial) {
        this.sectorial = sectorial;
    }

    public MunicipioEntity getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioEntity municipio) {
        this.municipio = municipio;
    }

    public List<InversionEntity> getInversiones() {
        return inversiones;
    }

    public void setInversiones(List<InversionEntity> inversiones) {
        this.inversiones = inversiones;
    }
}
