package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.domain.Empleado;
import com.tallerpicado.domain.Factura;
import com.tallerpicado.service.FacturaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall, insertarCall, actualizarCall, eliminarCall;
    private SimpleJdbcCall buscarPorClienteCall, buscarPorIdCall;

    @PostConstruct
    private void init() {
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_LISTAR_FACTURAS")
                .returningResultSet("p_resultado", new FacturaRowMapper());

        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_INSERTAR_FACTURA");

        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_ACTUALIZAR_FACTURA");

        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_ELIMINAR_FACTURA");

        buscarPorClienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withFunctionName("FN_BUSCAR_FACTURAS_POR_CLIENTE")
                .returningResultSet("return", new FacturaRowMapper());

        buscarPorIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withFunctionName("FN_BUSCAR_FACTURAS_POR_ID")
                .returningResultSet("return", new FacturaRowMapper());
    }

    @Override
    public List<Factura> listarTodas() {
        Map<String, Object> result = listarCall.execute();
        return (List<Factura>) result.get("p_resultado");
    }

    @Override
    public void insertar(Factura factura) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_fecha", factura.getFechaEmision());
        params.put("p_id_cliente", factura.getCliente().getId());
        params.put("p_id_empleado", factura.getEmpleado().getId());
        params.put("p_total", factura.getTotal());

        insertarCall.execute(params);
    }

    @Override
    public void actualizar(Long idFactura, Factura factura) {
        actualizarCall.execute(
                Map.of(
                        "p_id_factura", idFactura,
                        "p_fecha", factura.getFechaEmision(),
                        "p_id_cliente", factura.getCliente().getId(),
                        "p_id_empleado", factura.getEmpleado().getId(),
                        "p_total", factura.getTotal()
                )
        );
    }

    @Override
    public void eliminar(Long idFactura) {
        eliminarCall.execute(Map.of("p_id_factura", idFactura));
    }

    @Override
    public List<Factura> buscarPorCliente(String patron) {
        Map<String, Object> result = buscarPorClienteCall.execute(Map.of("p_patron", patron));
        return (List<Factura>) result.get("return");
    }

    @Override
    public List<Factura> buscarPorId(String patron) {
        Map<String, Object> result = buscarPorIdCall.execute(Map.of("p_patron", patron));
        return (List<Factura>) result.get("return");
    }

    @Override
    public Optional<Factura> obtenerPorId(Long id) {
        String sql = "SELECT F.ID_FACTURA, F.FECHA_EMISION, F.TOTAL, F.ID_CLIENTE, F.ID_EMPLEADO, "
                + "C.NOMBRE AS NOMBRE_CLIENTE, E.NOMBRE || ' ' || E.APELLIDO AS EMPLEADO "
                + "FROM FACTURAS F "
                + "JOIN CLIENTES C ON F.ID_CLIENTE = C.ID_CLIENTE "
                + "JOIN EMPLEADOS E ON F.ID_EMPLEADO = E.ID_EMPLEADO "
                + "WHERE F.ID_FACTURA = ?";

        try {
            Factura factura = jdbcTemplate.queryForObject(sql, new FacturaRowMapper(), id);
            return Optional.ofNullable(factura);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class FacturaRowMapper implements RowMapper<Factura> {

        @Override
        public Factura mapRow(ResultSet rs, int rowNum) throws SQLException {
            Factura f = new Factura();
            f.setId(rs.getLong("ID_FACTURA"));
            f.setFechaEmision(rs.getDate("FECHA_EMISION"));
            f.setTotal(rs.getDouble("TOTAL"));

            Cliente cliente = new Cliente();
            cliente.setId(rs.getLong("ID_CLIENTE"));
            try {
                cliente.setNombre(rs.getString("NOMBRE_CLIENTE"));
            } catch (SQLException ignored) {
            }
            f.setCliente(cliente);

            Empleado empleado = new Empleado();
            empleado.setId(rs.getLong("ID_EMPLEADO"));
            try {
                empleado.setNombre(rs.getString("EMPLEADO"));
            } catch (SQLException ignored) {
            }
            f.setEmpleado(empleado);

            return f;
        }
    }
}
