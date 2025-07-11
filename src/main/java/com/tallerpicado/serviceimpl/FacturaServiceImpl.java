package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Factura;
import com.tallerpicado.repository.FacturaRepository;
import com.tallerpicado.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }

    @Override
    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }

    @Override
    public Factura guardar(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public Factura actualizar(Long id, Factura factura) {
        Optional<Factura> actual = facturaRepository.findById(id);
        if (actual.isPresent()) {
            Factura f = actual.get();
            f.setFechaEmision(factura.getFechaEmision());
            f.setCliente(factura.getCliente());
            f.setEmpleado(factura.getEmpleado());
            f.setTotal(factura.getTotal());
            return facturaRepository.save(f);
        } else {
            throw new RuntimeException("Factura no encontrada: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }
}
