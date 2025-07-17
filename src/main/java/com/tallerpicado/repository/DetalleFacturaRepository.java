package com.tallerpicado.repository;

import com.tallerpicado.domain.DetalleFactura;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {

    public List<DetalleFactura> findByFacturaId(Long facturaId);
}
