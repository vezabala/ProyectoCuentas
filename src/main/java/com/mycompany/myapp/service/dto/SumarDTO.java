package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Sumar} entity.
 */
public class SumarDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer cantidadSumar;

    @NotNull
    private LocalDate fecha;

    private CantidadDTO cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadSumar() {
        return cantidadSumar;
    }

    public void setCantidadSumar(Integer cantidadSumar) {
        this.cantidadSumar = cantidadSumar;
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
        if (!(o instanceof SumarDTO)) {
            return false;
        }

        SumarDTO sumarDTO = (SumarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sumarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SumarDTO{" +
            "id=" + getId() +
            ", cantidadSumar=" + getCantidadSumar() +
            ", fecha='" + getFecha() + "'" +
            ", cantidad=" + getCantidad() +
            "}";
    }
}
