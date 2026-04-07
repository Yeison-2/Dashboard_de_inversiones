package com.jdc.web2026i.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "telefonos")
public class TelefonoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
    @JoinColumn(name = "id_seleccional", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SeleccionalEntity seleccional;

    // Columna legacy creada por cambios previos; se mantiene sincronizada con id_seleccional.
    @Column(name = "id_seleccionales", nullable = false)
    private Integer idSeccionalesLegacy;

    @ManyToOne
    @JoinColumn(name = "id_universidad", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UniversidadEntity universidad;

    @PrePersist
    @PreUpdate
    private void syncLegacyColumns() {
        if (seleccional != null) {
            this.idSeccionalesLegacy = seleccional.getIdSeleccional();
        }
    }
}
