package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Produccion;
import com.tallerpicado.repository.ProduccionRepository;
import com.tallerpicado.service.ProduccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduccionServiceImpl implements ProduccionService {

    @Autowired
    private ProduccionRepository produccionRepository;

    @Override
    public List<Produccion> obtenerTodas() {
        return produccionRepository.findAll();
    }

    @Override
    public Optional<Produccion> obtenerPorId(Long id) {
        return produccionRepository.findById(id);
    }

    @Override
    public Produccion guardar(Produccion produccion) {
        return produccionRepository.save(produccion);
    }

    @Override
    public Produccion actualizar(Long id, Produccion produccion) {
        Optional<Produccion> existente = produccionRepository.findById(id);
        if (existente.isPresent()) {
            Produccion p = existente.get();
            p.setMaquina(produccion.getMaquina());
            p.setOrden(produccion.getOrden());
            p.setFechaInicio(produccion.getFechaInicio());
            p.setFechaFin(produccion.getFechaFin());
            p.setEstado(produccion.getEstado());
            return produccionRepository.save(p);
        } else {
            throw new RuntimeException("Producción no encontrada con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        produccionRepository.deleteById(id);
    }
}
