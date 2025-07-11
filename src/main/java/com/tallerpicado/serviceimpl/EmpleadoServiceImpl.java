package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Empleado;
import com.tallerpicado.repository.EmpleadoRepository;
import com.tallerpicado.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> obtenerPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleado) {
        Optional<Empleado> existente = empleadoRepository.findById(id);
        if (existente.isPresent()) {
            Empleado e = existente.get();
            e.setNombre(empleado.getNombre());
            e.setApellido(empleado.getApellido());
            e.setIdPuesto(empleado.getIdPuesto());
            e.setCelular(empleado.getCelular());
            e.setCorreo(empleado.getCorreo());
            e.setEstado(empleado.getEstado());
            return empleadoRepository.save(e);
        } else {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }
}
