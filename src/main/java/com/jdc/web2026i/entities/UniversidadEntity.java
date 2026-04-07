package com.jdc.web2026i.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "universidades")

public class UniversidadEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUniversidad")
    private Integer idUniversidad;

    @NotNull
    @Size(min = 1,max = 50)
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Size(min = 0,max = 14)
    @Column(name = "nit")
    private String nit;

    @Column(name = "estado")
    private boolean estado;

    @NotNull
    @Column(name = "descripcion")
    @Size(min = 1, max = 200)
    private String descripcion;

    @OneToOne(mappedBy = "universidad", fetch = FetchType.LAZY)
    private RectorEntity rector;

    //conexiom con seleccionales
    @OneToMany(mappedBy = "universidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeleccionalEntity> seleccionales;


    public Integer getIdUniversidad() {
        return idUniversidad;
    }

    public void setIdUniversidad(Integer idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public RectorEntity getRector() {
        return rector;
    }

    public void setRector(RectorEntity rector) {
        this.rector = rector;
    }
}
