package com.jdc.web2026i.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;



@Entity
@Table(name = "telefonos")
public class TelefonoEntity implements Serializable {
    @Serial
    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTelefono")
    private Integer idTelefono;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numero")
    private String numero;

    @NotNull
    @Column(name = "tipo")
    @Size(min = 1, max = 20)
    private String tipo;



    @ManyToOne
    @JoinColumn(name = "idSeleccionales", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SeleccionalEntity seleccional;







}
