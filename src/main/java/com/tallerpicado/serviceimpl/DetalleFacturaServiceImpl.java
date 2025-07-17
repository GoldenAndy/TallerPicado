package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.repository.DetalleFacturaRepository;
import com.tallerpicado.service.DetalleFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleFacturaServiceImpl implements DetalleFacturaService {

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Override
    public List<DetalleFactura> obtenerTodos() {
        return detalleFacturaRepository.findAll();
    }

    @Override
    public List<DetalleFactura> obtenerPorFacturaId(Long facturaId) {
        // Implementación del método que faltaba
        return detalleFacturaRepository.findByFacturaId(facturaId);
    }

    @Override
    public Optional<DetalleFactura> obtenerPorId(Long id) {
        return detalleFacturaRepository.findById(id);
    }

    @Override
    public DetalleFactura guardar(DetalleFactura detalleFactura) {
        // Calcular subtotal si no viene ya calculado
        if (detalleFactura.getPrecioUnitario() != null && detalleFactura.getCantidad() != null) {
            detalleFactura.setSubtotal(detalleFactura.getPrecioUnitario() * detalleFactura.getCantidad());
        }
        return detalleFacturaRepository.save(detalleFactura);
    }

    @Override
    public DetalleFactura actualizar(Long id, DetalleFactura detalleFactura) {
        Optional<DetalleFactura> actual = detalleFacturaRepository.findById(id);
        if (actual.isPresent()) {
            DetalleFactura df = actual.get();
            df.setFactura(detalleFactura.getFactura()); // Asegúrate de que la factura no cambie
            df.setArticulo(detalleFactura.getArticulo());
            df.setCantidad(detalleFactura.getCantidad());
            df.setPrecioUnitario(detalleFactura.getPrecioUnitario());
            // Recalcular subtotal al actualizar
            if (df.getPrecioUnitario() != null && df.getCantidad() != null) {
                df.setSubtotal(df.getPrecioUnitario() * df.getCantidad());
            }
            return detalleFacturaRepository.save(df);
        } else {
            throw new RuntimeException("Detalle de Factura no encontrado: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        detalleFacturaRepository.deleteById(id);
    }
}
