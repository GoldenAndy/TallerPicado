package com.tallerpicado.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ProveedorEmpleadoId implements Serializable {

    private Long proveedor;
    private Long empleado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProveedorEmpleadoId)) return false;
        ProveedorEmpleadoId that = (ProveedorEmpleadoId) o;
        return Objects.equals(proveedor, that.proveedor) && Objects.equals(empleado, that.empleado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proveedor, empleado);
    }
}
