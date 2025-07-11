package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Mantenimiento;
import com.tallerpicado.repository.MantenimientoRepository;
import com.tallerpicado.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Override
    public List<Mantenimiento> obtenerTodos() {
        return mantenimientoRepository.findAll();
    }

    @Override
    public Optional<Mantenimiento> obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id);
    }

    @Override
    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public Mantenimiento actualizar(Long id, Mantenimiento mantenimiento) {
        Optional<Mantenimiento> existente = mantenimientoRepository.findById(id);
        if (existente.isPresent()) {
            Mantenimiento m = existente.get();
            m.setMaquina(mantenimiento.getMaquina());
            m.setFechaMantenimiento(mantenimiento.getFechaMantenimiento());
            m.setTipo(mantenimiento.getTipo());
            m.setDescripcion(mantenimiento.getDescripcion());
            return mantenimientoRepository.save(m);
        } else {
            throw new RuntimeException("Mantenimiento no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        mantenimientoRepository.deleteById(id);
    }
}
