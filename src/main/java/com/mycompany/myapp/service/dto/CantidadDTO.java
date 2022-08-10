package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Cantidad} entity.
 */
public class CantidadDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombreCuenta;

    @NotNull
    private Integer cantidadActual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public Integer getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CantidadDTO)) {
            return false;
        }

        CantidadDTO cantidadDTO = (CantidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cantidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CantidadDTO{" +
            "id=" + getId() +
            ", nombreCuenta='" + getNombreCuenta() + "'" +
            ", cantidadActual=" + getCantidadActual() +
            "}";
    }
}
