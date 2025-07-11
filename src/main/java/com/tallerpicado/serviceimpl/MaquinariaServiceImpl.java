package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.repository.MaquinariaRepository;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinariaServiceImpl implements MaquinariaService {

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    @Override
    public List<Maquinaria> obtenerTodas() {
        return maquinariaRepository.findAll();
    }

    @Override
    public Optional<Maquinaria> obtenerPorId(Long id) {
        return maquinariaRepository.findById(id);
    }

    @Override
    public Maquinaria guardar(Maquinaria maquinaria) {
        return maquinariaRepository.save(maquinaria);
    }

    @Override
    public Maquinaria actualizar(Long id, Maquinaria maquinaria) {
        Optional<Maquinaria> existente = maquinariaRepository.findById(id);
        if (existente.isPresent()) {
            Maquinaria m = existente.get();
            m.setNombre(maquinaria.getNombre());
            m.setMarca(maquinaria.getMarca());
            m.setFechaAdquisicion(maquinaria.getFechaAdquisicion());
            m.setEstado(maquinaria.getEstado());
            return maquinariaRepository.save(m);
        } else {
            throw new RuntimeException("Maquinaria no encontrada con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        maquinariaRepository.deleteById(id);
    }
}
