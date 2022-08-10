package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cantidad.
 */
@Entity
@Table(name = "cantidad")
public class Cantidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_cuenta", nullable = false)
    private String nombreCuenta;

    @NotNull
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;

    @OneToMany(mappedBy = "cantidad")
    @JsonIgnoreProperties(value = { "cantidad" }, allowSetters = true)
    private Set<Sumar> sumas = new HashSet<>();

    @OneToMany(mappedBy = "cantidad")
    @JsonIgnoreProperties(value = { "cantidad" }, allowSetters = true)
    private Set<Restar> restas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cantidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCuenta() {
        return this.nombreCuenta;
    }

    public Cantidad nombreCuenta(String nombreCuenta) {
        this.setNombreCuenta(nombreCuenta);
        return this;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public Integer getCantidadActual() {
        return this.cantidadActual;
    }

    public Cantidad cantidadActual(Integer cantidadActual) {
        this.setCantidadActual(cantidadActual);
        return this;
    }

    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public Set<Sumar> getSumas() {
        return this.sumas;
    }

    public void setSumas(Set<Sumar> sumars) {
        if (this.sumas != null) {
            this.sumas.forEach(i -> i.setCantidad(null));
        }
        if (sumars != null) {
            sumars.forEach(i -> i.setCantidad(this));
        }
        this.sumas = sumars;
    }

    public Cantidad sumas(Set<Sumar> sumars) {
        this.setSumas(sumars);
        return this;
    }

    public Cantidad addSumas(Sumar sumar) {
        this.sumas.add(sumar);
        sumar.setCantidad(this);
        return this;
    }

    public Cantidad removeSumas(Sumar sumar) {
        this.sumas.remove(sumar);
        sumar.setCantidad(null);
        return this;
    }

    public Set<Restar> getRestas() {
        return this.restas;
    }

    public void setRestas(Set<Restar> restars) {
        if (this.restas != null) {
            this.restas.forEach(i -> i.setCantidad(null));
        }
        if (restars != null) {
            restars.forEach(i -> i.setCantidad(this));
        }
        this.restas = restars;
    }

    public Cantidad restas(Set<Restar> restars) {
        this.setRestas(restars);
        return this;
    }

    public Cantidad addRestas(Restar restar) {
        this.restas.add(restar);
        restar.setCantidad(this);
        return this;
    }

    public Cantidad removeRestas(Restar restar) {
        this.restas.remove(restar);
        restar.setCantidad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cantidad)) {
            return false;
        }
        return id != null && id.equals(((Cantidad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cantidad{" +
            "id=" + getId() +
            ", nombreCuenta='" + getNombreCuenta() + "'" +
            ", cantidadActual=" + getCantidadActual() +
            "}";
    }
}
