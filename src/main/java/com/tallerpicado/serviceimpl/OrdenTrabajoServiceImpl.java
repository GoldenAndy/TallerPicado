package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.repository.OrdenTrabajoRepository;
import com.tallerpicado.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenTrabajoServiceImpl implements OrdenTrabajoService {

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public List<OrdenTrabajo> obtenerTodas() {
        return ordenTrabajoRepository.findAll();
    }

    @Override
    public Optional<OrdenTrabajo> obtenerPorId(Long id) {
        return ordenTrabajoRepository.findById(id);
    }

    @Override
    public OrdenTrabajo guardar(OrdenTrabajo orden) {
        return ordenTrabajoRepository.save(orden);
    }

    @Override
    public OrdenTrabajo actualizar(Long id, OrdenTrabajo orden) {
        Optional<OrdenTrabajo> existente = ordenTrabajoRepository.findById(id);
        if (existente.isPresent()) {
            OrdenTrabajo o = existente.get();
            o.setFecha(orden.getFecha());
            o.setCliente(orden.getCliente());
            o.setEmpleado(orden.getEmpleado());
            o.setEstado(orden.getEstado());
            return ordenTrabajoRepository.save(o);
        } else {
            throw new RuntimeException("Orden no encontrada con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        ordenTrabajoRepository.deleteById(id);
    }
}
