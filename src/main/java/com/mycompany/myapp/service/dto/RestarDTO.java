package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Restar} entity.
 */
public class RestarDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer cantidadRestar;

    @NotNull
    private LocalDate fecha;

    private CantidadDTO cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadRestar() {
        return cantidadRestar;
    }

    public void setCantidadRestar(Integer cantidadRestar) {
        this.cantidadRestar = cantidadRestar;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public CantidadDTO getCantidad() {
        return cantidad;
    }

    public void setCantidad(CantidadDTO cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestarDTO)) {
            return false;
        }

        RestarDTO restarDTO = (RestarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestarDTO{" +
            "id=" + getId() +
            ", cantidadRestar=" + getCantidadRestar() +
            ", fecha='" + getFecha() + "'" +
            ", cantidad=" + getCantidad() +
            "}";
    }
}
