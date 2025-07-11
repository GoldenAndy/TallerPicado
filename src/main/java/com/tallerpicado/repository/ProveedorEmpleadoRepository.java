package com.tallerpicado.repository;

import com.tallerpicado.domain.ProveedorEmpleado;
import com.tallerpicado.domain.ProveedorEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorEmpleadoRepository extends JpaRepository<ProveedorEmpleado, ProveedorEmpleadoId> {
}
