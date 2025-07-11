package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.repository.TipoMantenimientoRepository;
import com.tallerpicado.service.TipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoMantenimientoServiceImpl implements TipoMantenimientoService {

    @Autowired
    private TipoMantenimientoRepository repository;

    @Override
    public List<TipoMantenimiento> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<TipoMantenimiento> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public TipoMantenimiento guardar(TipoMantenimiento tipo) {
        return repository.save(tipo);
    }

    @Override
    public TipoMantenimiento actualizar(Long id, TipoMantenimiento tipo) {
        Optional<TipoMantenimiento> existente = repository.findById(id);
        if (existente.isPresent()) {
            TipoMantenimiento t = existente.get();
            t.setNombre(tipo.getNombre());
            return repository.save(t);
        } else {
            throw new RuntimeException("Tipo de mantenimiento no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
