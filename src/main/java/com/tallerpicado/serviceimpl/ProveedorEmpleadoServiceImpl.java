package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.ProveedorEmpleado;
import com.tallerpicado.domain.ProveedorEmpleadoId;
import com.tallerpicado.repository.ProveedorEmpleadoRepository;
import com.tallerpicado.service.ProveedorEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorEmpleadoServiceImpl implements ProveedorEmpleadoService {

    @Autowired
    private ProveedorEmpleadoRepository repository;

    @Override
    public List<ProveedorEmpleado> listarTodos() {
        return repository.findAll();
    }

    @Override
    public ProveedorEmpleado asignarEmpleado(ProveedorEmpleado relacion) {
        return repository.save(relacion);
    }

    @Override
    public void eliminarRelacion(Long idProveedor, Long idEmpleado) {
        ProveedorEmpleadoId id = new ProveedorEmpleadoId();
        id.setProveedor(idProveedor);
        id.setEmpleado(idEmpleado);
        repository.deleteById(id);
    }
}
