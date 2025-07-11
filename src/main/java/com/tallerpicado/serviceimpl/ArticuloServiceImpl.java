package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Articulo;
import com.tallerpicado.repository.ArticuloRepository;
import com.tallerpicado.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Override
    public List<Articulo> obtenerTodos() {
        return articuloRepository.findAll();
    }

    @Override
    public Optional<Articulo> obtenerPorId(Long id) {
        return articuloRepository.findById(id);
    }

    @Override
    public Articulo guardar(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    @Override
    public Articulo actualizar(Long id, Articulo articulo) {
        Optional<Articulo> existente = articuloRepository.findById(id);
        if (existente.isPresent()) {
            Articulo a = existente.get();
            a.setNombre(articulo.getNombre());
            a.setDescripcion(articulo.getDescripcion());
            a.setPrecio(articulo.getPrecio());
            a.setStock(articulo.getStock());
            a.setTipo(articulo.getTipo());
            return articuloRepository.save(a);
        } else {
            throw new RuntimeException("Artículo no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        articuloRepository.deleteById(id);
    }
}
