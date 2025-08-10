package com.tallerpicado.service;
 
import com.tallerpicado.domain.Factura;
import java.util.List;
import java.util.Optional;
 
public interface FacturaService {
    List<Factura> listarTodas();
    void insertar(Factura factura);
    void actualizar(Long idFactura, Factura factura);
    void eliminar(Long idFactura);
 
    List<Factura> buscarPorCliente(String patron);
    List<Factura> buscarPorId(String patron);

    public Optional<Factura> obtenerPorId(Long id);
}