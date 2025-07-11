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
    private DetalleFacturaRepository repository;

    @Override
    public List<DetalleFactura> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<DetalleFactura> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public DetalleFactura guardar(DetalleFactura detalle) {
        return repository.save(detalle);
    }

    @Override
    public DetalleFactura actualizar(Long id, DetalleFactura detalle) {
        Optional<DetalleFactura> actual = repository.findById(id);
        if (actual.isPresent()) {
            DetalleFactura d = actual.get();
            d.setFactura(detalle.getFactura());
            d.setArticulo(detalle.getArticulo());
            d.setCantidad(detalle.getCantidad());
            d.setPrecioUnitario(detalle.getPrecioUnitario());
            d.setSubtotal(detalle.getSubtotal());
            return repository.save(d);
        } else {
            throw new RuntimeException("Detalle no encontrado: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
