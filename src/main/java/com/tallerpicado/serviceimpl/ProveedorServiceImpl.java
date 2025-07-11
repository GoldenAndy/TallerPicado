package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Proveedor;
import com.tallerpicado.repository.ProveedorRepository;
import com.tallerpicado.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedor> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Long id, Proveedor proveedor) {
        Optional<Proveedor> existente = proveedorRepository.findById(id);
        if (existente.isPresent()) {
            Proveedor p = existente.get();
            p.setNombreEmpresa(proveedor.getNombreEmpresa());
            p.setCelular(proveedor.getCelular());
            p.setCorreo(proveedor.getCorreo());
            return proveedorRepository.save(p);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }
}
