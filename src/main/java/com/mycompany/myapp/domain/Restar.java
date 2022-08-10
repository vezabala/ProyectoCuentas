package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Restar.
 */
@Entity
@Table(name = "restar")
public class Restar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cantidad_restar", nullable = false)
    private Integer cantidadRestar;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sumas", "restas" }, allowSetters = true)
    private Cantidad cantidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadRestar() {
        return this.cantidadRestar;
    }

    public Restar cantidadRestar(Integer cantidadRestar) {
        this.setCantidadRestar(cantidadRestar);
        return this;
    }

    public void setCantidadRestar(Integer cantidadRestar) {
        this.cantidadRestar = cantidadRestar;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Restar fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cantidad getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(Cantidad cantidad) {
        this.cantidad = cantidad;
    }

    public Restar cantidad(Cantidad cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restar)) {
            return false;
        }
        return id != null && id.equals(((Restar) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restar{" +
            "id=" + getId() +
            ", cantidadRestar=" + getCantidadRestar() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
