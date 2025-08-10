package com.tallerpicado.service;
 
import com.tallerpicado.domain.DetalleFactura;
import java.util.List;
 
public interface DetalleFacturaService {
 

    Long insertar(DetalleFactura detalle);
    void actualizar(DetalleFactura detalle);
    void eliminar(Long idDetalle);
    List<DetalleFactura> listarPorFactura(Long idFactura);
    List<DetalleFactura> buscarPorArticulo(Long idFactura, String patron);
    Long guardar(DetalleFactura detalle);
}